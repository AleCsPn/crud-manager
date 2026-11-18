package model.dao;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import model.Company;
import model.Job;
import model.ModelException;

public class MySQLJobDAO implements JobDAO {

	// SELECT base reutilizado por listAll, listPage e findById.
	// O JOIN com companies traz o nome da empresa na mesma consulta
	// (evita fazer uma consulta extra para cada vaga listada).
	private static final String SELECT_JOBS = 
			" SELECT j.id AS job_id, j.title, j.salary, j.closing_date, j.remote, "
		  + "        c.id AS company_id, c.name AS company_name "
		  + " FROM jobs j "
		  + " INNER JOIN companies c ON j.company_id = c.id ";

	@Override
	public boolean save(Job job) throws ModelException {
		DBHandler db = new DBHandler();

		String sqlInsert = "INSERT INTO jobs "
				+ " (title, salary, closing_date, remote, company_id) "
				+ " VALUES (?, ?, ?, ?, ?);";

		db.prepareStatement(sqlInsert);

		db.setString(1, job.getTitle());
		db.setBigDecimal(2, job.getSalary());
		db.setDate(3, job.getClosingDate());
		db.setBoolean(4, job.isRemote());
		db.setInt(5, job.getCompany().getId());

		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(Job job) throws ModelException {
		DBHandler db = new DBHandler();

		String sqlUpdate = "UPDATE jobs "
				+ " SET title = ?, "
				+ "     salary = ?, "
				+ "     closing_date = ?, "
				+ "     remote = ?, "
				+ "     company_id = ? "
				+ " WHERE id = ?;";

		db.prepareStatement(sqlUpdate);

		db.setString(1, job.getTitle());
		db.setBigDecimal(2, job.getSalary());
		db.setDate(3, job.getClosingDate());
		db.setBoolean(4, job.isRemote());
		db.setInt(5, job.getCompany().getId());
		db.setInt(6, job.getId());

		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Job job) throws ModelException {
		DBHandler db = new DBHandler();

		String sqlDelete = " DELETE FROM jobs WHERE id = ?;";

		db.prepareStatement(sqlDelete);
		db.setInt(1, job.getId());

		try {
			return db.executeUpdate() > 0;
		} catch (ModelException e) {
			// Vaga com candidatos (candidates.job_id) não pode ser excluída:
			// devolve false e o controller mostra a mensagem amigável
			if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
				return false;
			}

			throw e;
		}
	}

	@Override
	public List<Job> listAll() throws ModelException {
		DBHandler db = new DBHandler();

		List<Job> jobs = new ArrayList<Job>();

		db.createStatement();
		db.executeQuery(SELECT_JOBS + " ORDER BY j.title, j.id");

		while (db.next()) {
			jobs.add(createJob(db));
		}

		return jobs;
	}

	@Override
	public List<Job> listPage(int page, int pageSize) throws ModelException {
		DBHandler db = new DBHandler();

		List<Job> jobs = new ArrayList<Job>();

		// LIMIT = quantos registros trazer; OFFSET = quantos pular
		// Página 1 -> OFFSET 0, página 2 -> OFFSET pageSize, e assim por diante
		db.prepareStatement(SELECT_JOBS + " ORDER BY j.title, j.id LIMIT ? OFFSET ?");
		db.setInt(1, pageSize);
		db.setInt(2, (Math.max(page, 1) - 1) * pageSize);
		db.executeQuery();

		while (db.next()) {
			jobs.add(createJob(db));
		}

		return jobs;
	}

	@Override
	public int countAll() throws ModelException {
		DBHandler db = new DBHandler();

		db.createStatement();
		db.executeQuery("SELECT COUNT(*) AS total FROM jobs");

		int total = db.next() ? db.getInt("total") : 0;
		db.close();

		return total;
	}

	@Override
	public Job findById(int id) throws ModelException {
		DBHandler db = new DBHandler();

		db.prepareStatement(SELECT_JOBS + " WHERE j.id = ?");
		db.setInt(1, id);
		db.executeQuery();

		Job job = null;
		if (db.next()) {
			job = createJob(db);
		}
		db.close();

		return job;
	}

	// Monta Job + Company a partir de uma linha do JOIN jobs x companies
	private Job createJob(DBHandler db) throws ModelException {
		Company company = new Company(db.getInt("company_id"));
		company.setName(db.getString("company_name"));

		Job job = new Job(db.getInt("job_id"));
		job.setTitle(db.getString("title"));
		job.setSalary(db.getBigDecimal("salary"));
		job.setClosingDate(db.getDate("closing_date"));
		job.setRemote(db.getBoolean("remote"));
		job.setCompany(company);

		return job;
	}
}
