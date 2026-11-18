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

	// Nome do atributo de sessão que guarda a conta logada (o filtro usa o mesmo nome)
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
			// Quem já está logado não precisa ver a tela de login
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
				// Cria a sessão (se ainda não existe) e troca o ID dela logo após 
				// autenticar. Evita "session fixation": um ID de sessão que o 
				// atacante conhecia antes do login deixa de valer.
				req.getSession();
				req.changeSessionId();

				// O hash da senha não precisa ficar guardado na sessão
				account.setPasswordHash(null);
				req.getSession().setAttribute(SESSION_ATTRIBUTE, account);

				// Redirect (e não forward): se o usuário apertar F5, o navegador 
				// não reenvia o POST de login. Padrão Post/Redirect/Get.
				ControllerUtil.redirect(resp, req.getContextPath() + "/posts");
				return;
			}

			// Mesma mensagem para e-mail inexistente e senha errada: não revela 
			// quais e-mails estão cadastrados
			showLoginError(req, resp, email, "E-mail ou senha inválidos.");

		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			showLoginError(req, resp, email, "Não foi possível realizar o login. Tente novamente.");
		}
	}

	private void showLoginError(HttpServletRequest req, HttpServletResponse resp, 
			String email, String message) throws ServletException, IOException {

		req.setAttribute("error", message);
		// devolve o e-mail digitado para o usuário não ter que digitar de novo
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
