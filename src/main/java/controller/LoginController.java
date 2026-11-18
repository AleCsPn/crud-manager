package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.ModelException;
import model.dao.AccountDAO;
import model.dao.DAOFactory;
import util.PasswordUtil;

@WebServlet(urlPatterns = { "/login", "/logout" })
public class LoginController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String SESSION_ATTRIBUTE = "loggedAccount";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/crud-manager/logout": {
			logout(req, resp);
			break;
		}
		default:
			HttpSession session = req.getSession(false);
			if (session != null && session.getAttribute(SESSION_ATTRIBUTE) != null) {
				ControllerUtil.redirect(resp, req.getContextPath() + "/posts");
				return;
			}

			ControllerUtil.forward(req, resp, "/login.jsp");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/crud-manager/login": {
			authenticate(req, resp);
			break;
		}
		default:
			ControllerUtil.redirect(resp, req.getContextPath() + "/login");
		}
	}

	private void authenticate(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String email = req.getParameter("email");
		String password = req.getParameter("password");

		if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
			showLoginError(req, resp, email, "Informe o e-mail e a senha.");
			return;
		}

		AccountDAO dao = DAOFactory.createDAO(AccountDAO.class);

		try {
			Account account = dao.findByEmail(email.trim());

			if (account != null && PasswordUtil.verify(password, account.getPasswordHash())) {
				req.getSession();
				req.changeSessionId();

				account.setPasswordHash(null);
				req.getSession().setAttribute(SESSION_ATTRIBUTE, account);

				ControllerUtil.redirect(resp, req.getContextPath() + "/posts");
				return;
			}

			showLoginError(req, resp, email, "E-mail ou senha inválidos.");

		} catch (ModelException e) {
			e.printStackTrace();
			showLoginError(req, resp, email, "Não foi possível realizar o login. Tente novamente.");
		}
	}

	private void showLoginError(HttpServletRequest req, HttpServletResponse resp, 
			String email, String message) throws ServletException, IOException {

		req.setAttribute("error", message);
		req.setAttribute("email", email);

		ControllerUtil.forward(req, resp, "/login.jsp");
	}

	private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession(false);

		if (session != null)
			session.invalidate();

		ControllerUtil.redirect(resp, req.getContextPath() + "/login?logout=true");
	}
}
