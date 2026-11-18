package controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import model.Company;
import model.Job;
import model.ModelException;
import model.User;
import model.dao.CompanyDAO;
import model.dao.DAOFactory;
import model.dao.JobDAO;
import model.dao.UserDAO;

public class CommonsController {
	
	public static void listUsers(HttpServletRequest req) {
		UserDAO dao = DAOFactory.createDAO(UserDAO.class);
		
		List<User> listUsers = null;
		try {
			listUsers = dao.listAll();
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		if (listUsers != null)
			req.setAttribute("users", listUsers);		
	}
	
	// Carrega todas as empresas para preencher o <select> do formulário de vagas
	public static void listCompanies(HttpServletRequest req) {
		CompanyDAO dao = DAOFactory.createDAO(CompanyDAO.class);
		
		List<Company> listCompanies = null;
		try {
			listCompanies = dao.listAll();
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		if (listCompanies != null)
			req.setAttribute("companies", listCompanies);		
	}
	
	// Carrega todas as vagas para preencher o <select> do formulário de candidatos
	public static void listJobs(HttpServletRequest req) {
		JobDAO dao = DAOFactory.createDAO(JobDAO.class);
		
		List<Job> listJobs = null;
		try {
			listJobs = dao.listAll();
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		if (listJobs != null)
			req.setAttribute("jobs", listJobs);		
	}
}
