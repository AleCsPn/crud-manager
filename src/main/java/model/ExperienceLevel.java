package model;

/**
 * Nível de experiência do candidato.
 * O nome da constante (JUNIOR, PLENO, SENIOR) é o que vai para o banco (ENUM) 
 * e para o value do input radio; o label é o texto exibido ao usuário.
 */
public enum ExperienceLevel {
	JUNIOR("Júnior"), 
	PLENO("Pleno"), 
	SENIOR("Sênior");

	private final String label;

	private ExperienceLevel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
