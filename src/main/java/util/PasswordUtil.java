package util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Geração e verificação de hash de senhas usando PBKDF2 (HMAC-SHA256),
 * que já vem no JDK (não precisa de biblioteca externa).
 * 
 * Nunca guardamos a senha em texto puro no banco. Guardamos uma String no 
 * formato:  pbkdf2_sha256$ITERAÇÕES$SALT(base64)$HASH(base64)
 * 
 * - SALT: bytes aleatórios, diferentes para cada senha. Garante que duas 
 *   pessoas com a mesma senha tenham hashes diferentes e inutiliza tabelas 
 *   pré-calculadas (rainbow tables).
 * - ITERAÇÕES: o algoritmo é repetido muitas vezes de propósito, para tornar 
 *   lento um ataque de força bruta.
 * 
 * Para gerar o hash de uma senha (ex.: para inserir outro usuário no banco):
 *     java -cp WEB-INF/classes util.PasswordUtil minhaSenha
 */
public class PasswordUtil {

	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final String PREFIX = "pbkdf2_sha256";
	private static final int ITERATIONS = 120_000;
	private static final int SALT_BYTES = 16;
	private static final int KEY_BITS = 256;

	private PasswordUtil() {
	}

	// Gera o hash (com salt novo) para guardar no banco
	public static String hash(String password) {
		byte[] salt = new byte[SALT_BYTES];
		new SecureRandom().nextBytes(salt);

		byte[] hash = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_BITS);

		return PREFIX + "$" + ITERATIONS + "$" 
				+ Base64.getEncoder().encodeToString(salt) + "$" 
				+ Base64.getEncoder().encodeToString(hash);
	}

	// Confere se a senha digitada corresponde ao hash guardado no banco
	public static boolean verify(String password, String stored) {
		if (password == null || stored == null)
			return false;

		String[] parts = stored.split("\\$");
		if (parts.length != 4 || !PREFIX.equals(parts[0]))
			return false;

		try {
			int iterations = Integer.parseInt(parts[1]);
			byte[] salt = Base64.getDecoder().decode(parts[2]);
			byte[] expected = Base64.getDecoder().decode(parts[3]);

			byte[] actual = pbkdf2(password.toCharArray(), salt, iterations, expected.length * 8);

			// Comparação em tempo constante (não "vaza" em qual byte divergiu)
			return MessageDigest.isEqual(expected, actual);
		} catch (IllegalArgumentException e) {
			// hash armazenado em formato inválido
			return false;
		}
	}

	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyBits) {
		try {
			PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyBits);
			return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Algoritmo de hash indisponível", e);
		}
	}

	// Utilitário de linha de comando para gerar hashes
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Uso: java util.PasswordUtil <senha>");
			return;
		}
		System.out.println(hash(args[0]));
	}
}
