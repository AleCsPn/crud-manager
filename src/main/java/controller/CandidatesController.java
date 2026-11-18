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
import model.Candidate;
import model.ExperienceLevel;
import model.Job;
import model.ModelException;
import model.dao.CandidateDAO;
import model.dao.DAOFactory;
import model.dao.JobDAO;
import util.Pagination;

@WebServlet(urlPatterns = { "/candidates", "/candidate/form", "/candidate/insert", 
		"/candidate/delete", "/candidate/update" })
public class CandidatesController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// A coluna é DECIMAL(10,2): o maior valor possível é 99.999.999,99
	private static final BigDecimal MAX_SALARY = new BigDecimal("99999999.99");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/crud-manager/candidate/form": {
			loadFormData(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-candidate.jsp");
			break;
		}
		case "/crud-manager/candidate/update": {
			Candidate candidate = loadCandidate(req);

			if (candidate == null) {
				ControllerUtil.redirect(resp, req.getContextPath() + "/candidates");
				return;
			}

			loadFormData(req);
			req.setAttribute("action", "update");
			req.setAttribute("candidate", candidate);
			ControllerUtil.forward(req, resp, "/form-candidate.jsp");
			break;
		}
		default:
			listCandidates(req);

			ControllerUtil.transferSessionMessagesToRequest(req);

			ControllerUtil.forward(req, resp, "/candidates.jsp");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/crud-manager/candidate/insert": {
			insertCandidate(req, resp);
			break;
		}
		case "/crud-manager/candidate/delete": {
			deleteCandidate(req, resp);
			break;
		}
		case "/crud-manager/candidate/update": {
			updateCandidate(req, resp);
			break;
		}
		default:
			System.out.println("URL inválida " + action);
		}

		ControllerUtil.redirect(resp, req.getContextPath() + "/candidates");
	}

	// Dados que o formulário precisa: as vagas (para o <select>) e os 
	// níveis de experiência (para os inputs radio)
	private void loadFormData(HttpServletRequest req) {
		CommonsController.listJobs(req);
		req.setAttribute("levels", ExperienceLevel.values());
	}

	private void listCandidates(HttpServletRequest req) {
		CandidateDAO dao = DAOFactory.createDAO(CandidateDAO.class);

		List<Candidate> candidates = null;
		try {
			// conta o total, calcula a página atual e busca só os registros dela
			Pagination pagination = ControllerUtil.createPagination(req, dao.countAll());

			candidates = dao.listPage(pagination.getCurrentPage(), pagination.getPageSize());
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}

		if (candidates != null)
			req.setAttribute("candidates", candidates);
	}

	private Candidate loadCandidate(HttpServletRequest req) {
		try {
			int candidateId = Integer.parseInt(req.getParameter("candidateId"));

			CandidateDAO dao = DAOFactory.createDAO(CandidateDAO.class);
			Candidate candidate = dao.findById(candidateId);

			if (candidate == null)
				throw new ModelException("Candidato não encontrado para alteração.");

			return candidate;
		} catch (NumberFormatException e) {
			ControllerUtil.errorMessage(req, "Candidato inválido.");
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}

		return null;
	}

	/**
	 * Lê os campos do formulário, valida e preenche o objeto Candidate.
	 * Devolve null se está tudo certo, ou o texto do erro a ser mostrado.
	 */
	private String fillCandidate(HttpServletRequest req, Candidate candidate) 
			throws ModelException {

		String fullName = req.getParameter("fullName");
		String salaryStr = req.getParameter("salaryExpectation");
		String birthDateStr = req.getParameter("birthDate");
		String levelStr = req.getParameter("level");
		String jobStr = req.getParameter("job");

		if (fullName == null || fullName.trim().isEmpty())
			return "Informe o nome completo do candidato.";

		if (fullName.trim().length() > 150)
			return "O nome pode ter no máximo 150 caracteres.";

		BigDecimal salary;
		try {
			salary = new BigDecimal(salaryStr.trim()).setScale(2, RoundingMode.HALF_UP);
		} catch (NumberFormatException | NullPointerException e) {
			return "Informe uma pretensão salarial válida.";
		}

		if (salary.signum() < 0)
			return "A pretensão salarial não pode ser negativa.";

		if (salary.compareTo(MAX_SALARY) > 0)
			return "A pretensão salarial informada é muito alta.";

		Date birthDate = ControllerUtil.formatDate(birthDateStr);
		if (birthDate == null)
			return "Informe uma data de nascimento válida.";

		if (birthDate.after(new Date()))
			return "A data de nascimento não pode ser no futuro.";

		// Radio: se nenhuma opção for marcada o parâmetro nem chega (null)
		ExperienceLevel level;
		try {
			level = ExperienceLevel.valueOf(levelStr);
		} catch (IllegalArgumentException | NullPointerException e) {
			return "Selecione o nível de experiência.";
		}

		int jobId;
		try {
			jobId = Integer.parseInt(jobStr);
		} catch (NumberFormatException e) {
			return "Selecione a vaga desejada.";
		}

		// Confere se a vaga realmente existe (o valor do <select> pode ser adulterado)
		JobDAO jobDAO = DAOFactory.createDAO(JobDAO.class);
		Job job = jobDAO.findById(jobId);
		if (job == null)
			return "A vaga selecionada não existe mais.";

		candidate.setFullName(fullName.trim());
		candidate.setSalaryExpectation(salary);
		candidate.setBirthDate(birthDate);
		candidate.setExperienceLevel(level);
		candidate.setDesiredJob(job);

		return null;
	}

	private void insertCandidate(HttpServletRequest req, HttpServletResponse resp) {
		Candidate candidate = new Candidate();

		CandidateDAO dao = DAOFactory.createDAO(CandidateDAO.class);

		try {
			String error = fillCandidate(req, candidate);
			if (error != null) {
				ControllerUtil.errorMessage(req, error);
				return;
			}

			if (dao.save(candidate)) {
				ControllerUtil.sucessMessage(req, "Candidato '" + candidate.getFullName() 
						+ "' salvo com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Candidato '" + candidate.getFullName() 
						+ "' não pode ser salvo.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void updateCandidate(HttpServletRequest req, HttpServletResponse resp) {
		int candidateId;
		try {
			candidateId = Integer.parseInt(req.getParameter("candidateId"));
		} catch (NumberFormatException e) {
			ControllerUtil.errorMessage(req, "Candidato inválido.");
			return;
		}

		Candidate candidate = new Candidate(candidateId);

		CandidateDAO dao = DAOFactory.createDAO(CandidateDAO.class);

		try {
			String error = fillCandidate(req, candidate);
			if (error != null) {
				ControllerUtil.errorMessage(req, error);
				return;
			}

			if (dao.update(candidate)) {
				ControllerUtil.sucessMessage(req, "Candidato '" + candidate.getFullName() 
						+ "' atualizado com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Candidato '" + candidate.getFullName() 
						+ "' não pode ser atualizado.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void deleteCandidate(HttpServletRequest req, HttpServletResponse resp) {
		CandidateDAO dao = DAOFactory.createDAO(CandidateDAO.class);

		try {
			int candidateId = Integer.parseInt(req.getParameter("id"));

			Candidate candidate = dao.findById(candidateId);

			if (candidate == null)
				throw new ModelException("Candidato não encontrado para deleção.");

			if (dao.delete(candidate)) {
				ControllerUtil.sucessMessage(req, "Candidato '" + candidate.getFullName() 
						+ "' deletado com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Candidato '" + candidate.getFullName() 
						+ "' não pode ser deletado.");
			}
		} catch (NumberFormatException e) {
			ControllerUtil.errorMessage(req, "Candidato inválido.");
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}
}
