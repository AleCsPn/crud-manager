package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Candidato a uma vaga. Cada candidato está vinculado à vaga desejada (Job).
 */
public class Candidate {
	private int id;
	private String fullName;
	private BigDecimal salaryExpectation;
	private Date birthDate;
	private ExperienceLevel experienceLevel;
	private Job desiredJob;

	public Candidate() {
		this(0);
	}

	public Candidate(int id) {
		this.id = id;
		this.fullName = "";
		this.salaryExpectation = BigDecimal.ZERO;
	}

	public int getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public BigDecimal getSalaryExpectation() {
		return salaryExpectation;
	}

	public void setSalaryExpectation(BigDecimal salaryExpectation) {
		this.salaryExpectation = salaryExpectation;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public ExperienceLevel getExperienceLevel() {
		return experienceLevel;
	}

	public void setExperienceLevel(ExperienceLevel experienceLevel) {
		this.experienceLevel = experienceLevel;
	}

	public Job getDesiredJob() {
		return desiredJob;
	}

	public void setDesiredJob(Job desiredJob) {
		this.desiredJob = desiredJob;
	}
}
