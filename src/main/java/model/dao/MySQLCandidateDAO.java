package model.dao;

import java.util.ArrayList;
import java.util.List;

import model.Candidate;
import model.Company;
import model.ExperienceLevel;
import model.Job;
import model.ModelException;

public class MySQLCandidateDAO implements CandidateDAO {

	private static final String SELECT_CANDIDATES = 
			" SELECT ca.id AS candidate_id, ca.full_name, ca.salary_expectation, "
		  + "        ca.birth_date, ca.experience_level, "
		  + "        j.id AS job_id, j.title AS job_title, "
		  + "        c.id AS company_id, c.name AS company_name "
		  + " FROM candidates ca "
		  + " INNER JOIN jobs j ON ca.job_id = j.id "
		  + " INNER JOIN companies c ON j.company_id = c.id ";

	@Override
	public boolean save(Candidate candidate) throws ModelException {
		DBHandler db = new DBHandler();

		String sqlInsert = "INSERT INTO candidates "
				+ " (full_name, salary_expectation, birth_date, experience_level, job_id) "
				+ " VALUES (?, ?, ?, ?, ?);";

		db.prepareStatement(sqlInsert);

		db.setString(1, candidate.getFullName());
		db.setBigDecimal(2, candidate.getSalaryExpectation());
		db.setDate(3, candidate.getBirthDate());
		db.setString(4, candidate.getExperienceLevel().name());
		db.setInt(5, candidate.getDesiredJob().getId());

		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(Candidate candidate) throws ModelException {
		DBHandler db = new DBHandler();

		String sqlUpdate = "UPDATE candidates "
				+ " SET full_name = ?, "
				+ "     salary_expectation = ?, "
				+ "     birth_date = ?, "
				+ "     experience_level = ?, "
				+ "     job_id = ? "
				+ " WHERE id = ?;";

		db.prepareStatement(sqlUpdate);

		db.setString(1, candidate.getFullName());
		db.setBigDecimal(2, candidate.getSalaryExpectation());
		db.setDate(3, candidate.getBirthDate());
		db.setString(4, candidate.getExperienceLevel().name());
		db.setInt(5, candidate.getDesiredJob().getId());
		db.setInt(6, candidate.getId());

		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Candidate candidate) throws ModelException {
		DBHandler db = new DBHandler();

		String sqlDelete = " DELETE FROM candidates WHERE id = ?;";

		db.prepareStatement(sqlDelete);
		db.setInt(1, candidate.getId());

		return db.executeUpdate() > 0;
	}

	@Override
	public List<Candidate> listAll() throws ModelException {
		DBHandler db = new DBHandler();

		List<Candidate> candidates = new ArrayList<Candidate>();

		db.createStatement();
		db.executeQuery(SELECT_CANDIDATES + " ORDER BY ca.full_name, ca.id");

		while (db.next()) {
			candidates.add(createCandidate(db));
		}

		return candidates;
	}

	@Override
	public List<Candidate> listPage(int page, int pageSize) throws ModelException {
		DBHandler db = new DBHandler();

		List<Candidate> candidates = new ArrayList<Candidate>();

		db.prepareStatement(SELECT_CANDIDATES + " ORDER BY ca.full_name, ca.id LIMIT ? OFFSET ?");
		db.setInt(1, pageSize);
		db.setInt(2, (Math.max(page, 1) - 1) * pageSize);
		db.executeQuery();

		while (db.next()) {
			candidates.add(createCandidate(db));
		}

		return candidates;
	}

	@Override
	public int countAll() throws ModelException {
		DBHandler db = new DBHandler();

		db.createStatement();
		db.executeQuery("SELECT COUNT(*) AS total FROM candidates");

		int total = db.next() ? db.getInt("total") : 0;
		db.close();

		return total;
	}

	@Override
	public Candidate findById(int id) throws ModelException {
		DBHandler db = new DBHandler();

		db.prepareStatement(SELECT_CANDIDATES + " WHERE ca.id = ?");
		db.setInt(1, id);
		db.executeQuery();

		Candidate candidate = null;
		if (db.next()) {
			candidate = createCandidate(db);
		}
		db.close();

		return candidate;
	}

	private Candidate createCandidate(DBHandler db) throws ModelException {
		Company company = new Company(db.getInt("company_id"));
		company.setName(db.getString("company_name"));

		Job job = new Job(db.getInt("job_id"));
		job.setTitle(db.getString("job_title"));
		job.setCompany(company);

		Candidate candidate = new Candidate(db.getInt("candidate_id"));
		candidate.setFullName(db.getString("full_name"));
		candidate.setSalaryExpectation(db.getBigDecimal("salary_expectation"));
		candidate.setBirthDate(db.getDate("birth_date"));
		candidate.setExperienceLevel(ExperienceLevel.valueOf(db.getString("experience_level")));
		candidate.setDesiredJob(job);

		return candidate;
	}
}
