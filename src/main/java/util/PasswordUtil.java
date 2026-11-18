package util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final String PREFIX = "pbkdf2_sha256";
	private static final int ITERATIONS = 120_000;
	private static final int SALT_BYTES = 16;
	private static final int KEY_BITS = 256;

	private PasswordUtil() {
	}

	public static String hash(String password) {
		byte[] salt = new byte[SALT_BYTES];
		new SecureRandom().nextBytes(salt);

		byte[] hash = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_BITS);

		return PREFIX + "$" + ITERATIONS + "$" 
				+ Base64.getEncoder().encodeToString(salt) + "$" 
				+ Base64.getEncoder().encodeToString(hash);
	}

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

			return MessageDigest.isEqual(expected, actual);
		} catch (IllegalArgumentException e) {
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

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Uso: java util.PasswordUtil <senha>");
			return;
		}
		System.out.println(hash(args[0]));
	}
}
