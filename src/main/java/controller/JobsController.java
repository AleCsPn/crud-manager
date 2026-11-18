package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Company;
import model.Job;
import model.ModelException;
import model.dao.CompanyDAO;
import model.dao.DAOFactory;
import model.dao.JobDAO;
import util.Pagination;

@WebServlet(urlPatterns = { "/jobs", "/job/form", "/job/insert", "/job/delete", "/job/update" })
public class JobsController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final BigDecimal MAX_SALARY = new BigDecimal("99999999.99");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/crud-manager/job/form": {
			CommonsController.listCompanies(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-job.jsp");
			break;
		}
		case "/crud-manager/job/update": {
			Job job = loadJob(req);

			if (job == null) {
				ControllerUtil.redirect(resp, req.getContextPath() + "/jobs");
				return;
			}

			CommonsController.listCompanies(req);
			req.setAttribute("action", "update");
			req.setAttribute("job", job);
			ControllerUtil.forward(req, resp, "/form-job.jsp");
			break;
		}
		default:
			listJobs(req);

			ControllerUtil.transferSessionMessagesToRequest(req);

			ControllerUtil.forward(req, resp, "/jobs.jsp");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/crud-manager/job/insert": {
			insertJob(req, resp);
			break;
		}
		case "/crud-manager/job/delete": {
			deleteJob(req, resp);
			break;
		}
		case "/crud-manager/job/update": {
			updateJob(req, resp);
			break;
		}
		default:
			System.out.println("URL inválida " + action);
		}

		ControllerUtil.redirect(resp, req.getContextPath() + "/jobs");
	}

	private void listJobs(HttpServletRequest req) {
		JobDAO dao = DAOFactory.createDAO(JobDAO.class);

		List<Job> jobs = null;
		try {
			Pagination pagination = ControllerUtil.createPagination(req, dao.countAll());

			jobs = dao.listPage(pagination.getCurrentPage(), pagination.getPageSize());
		} catch (ModelException e) {
			e.printStackTrace();
		}

		if (jobs != null)
			req.setAttribute("jobs", jobs);
	}

	private Job loadJob(HttpServletRequest req) {
		try {
			int jobId = Integer.parseInt(req.getParameter("jobId"));

			JobDAO dao = DAOFactory.createDAO(JobDAO.class);
			Job job = dao.findById(jobId);

			if (job == null)
				throw new ModelException("Vaga não encontrada para alteração.");

			return job;
		} catch (NumberFormatException e) {
			ControllerUtil.errorMessage(req, "Vaga inválida.");
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}

		return null;
	}

	private String fillJob(HttpServletRequest req, Job job) throws ModelException {
		String title = req.getParameter("title");
		String salaryStr = req.getParameter("salary");
		String closingDateStr = req.getParameter("closingDate");
		String companyStr = req.getParameter("company");

		boolean remote = req.getParameter("remote") != null;

		if (title == null || title.trim().isEmpty())
			return "Informe o título da vaga.";

		if (title.trim().length() > 150)
			return "O título da vaga pode ter no máximo 150 caracteres.";

		BigDecimal salary;
		try {
			salary = new BigDecimal(salaryStr.trim()).setScale(2, RoundingMode.HALF_UP);
		} catch (NumberFormatException | NullPointerException e) {
			return "Informe um salário válido.";
		}

		if (salary.signum() < 0)
			return "O salário não pode ser negativo.";

		if (salary.compareTo(MAX_SALARY) > 0)
			return "O salário informado é muito alto.";

		Date closingDate = ControllerUtil.formatDate(closingDateStr);
		if (closingDate == null)
			return "Informe uma data de encerramento válida.";

		int companyId;
		try {
			companyId = Integer.parseInt(companyStr);
		} catch (NumberFormatException e) {
			return "Selecione a empresa contratante.";
		}

		CompanyDAO companyDAO = DAOFactory.createDAO(CompanyDAO.class);
		Company company = companyDAO.findById(companyId);
		if (company == null)
			return "A empresa selecionada não existe mais.";

		job.setTitle(title.trim());
		job.setSalary(salary);
		job.setClosingDate(closingDate);
		job.setRemote(remote);
		job.setCompany(company);

		return null;
	}

	private void insertJob(HttpServletRequest req, HttpServletResponse resp) {
		Job job = new Job();

		JobDAO dao = DAOFactory.createDAO(JobDAO.class);

		try {
			String error = fillJob(req, job);
			if (error != null) {
				ControllerUtil.errorMessage(req, error);
				return;
			}

			if (dao.save(job)) {
				ControllerUtil.sucessMessage(req, "Vaga '" + job.getTitle() 
						+ "' salva com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Vaga '" + job.getTitle() 
						+ "' não pode ser salva.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void updateJob(HttpServletRequest req, HttpServletResponse resp) {
		int jobId;
		try {
			jobId = Integer.parseInt(req.getParameter("jobId"));
		} catch (NumberFormatException e) {
			ControllerUtil.errorMessage(req, "Vaga inválida.");
			return;
		}

		Job job = new Job(jobId);

		JobDAO dao = DAOFactory.createDAO(JobDAO.class);

		try {
			String error = fillJob(req, job);
			if (error != null) {
				ControllerUtil.errorMessage(req, error);
				return;
			}

			if (dao.update(job)) {
				ControllerUtil.sucessMessage(req, "Vaga '" + job.getTitle() 
						+ "' atualizada com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Vaga '" + job.getTitle() 
						+ "' não pode ser atualizada.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void deleteJob(HttpServletRequest req, HttpServletResponse resp) {
		JobDAO dao = DAOFactory.createDAO(JobDAO.class);

		try {
			int jobId = Integer.parseInt(req.getParameter("id"));

			Job job = dao.findById(jobId);

			if (job == null)
				throw new ModelException("Vaga não encontrada para deleção.");

			if (dao.delete(job)) {
				ControllerUtil.sucessMessage(req, "Vaga '" + job.getTitle() 
						+ "' deletada com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Vaga '" + job.getTitle() 
						+ "' não pode ser deletada. Há candidatos relacionados à vaga.");
			}
		} catch (NumberFormatException e) {
			ControllerUtil.errorMessage(req, "Vaga inválida.");
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}
}
