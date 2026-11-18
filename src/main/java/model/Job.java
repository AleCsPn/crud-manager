package model;

import java.math.BigDecimal;
import java.util.Date;

public class Job {
	private int id;
	private String title;
	private BigDecimal salary;
	private Date closingDate;
	private boolean remote;
	private Company company;

	public Job() {
		this(0);
	}

	public Job(int id) {
		this.id = id;
		this.title = "";
		this.salary = BigDecimal.ZERO;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		this.remote = remote;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
