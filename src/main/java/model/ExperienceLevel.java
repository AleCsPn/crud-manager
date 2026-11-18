package model;

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
