package controller;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

	private static final String[] PUBLIC_PREFIXES = { "/css/", "/js/", "/fonts/", "/images/" };

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, 
			FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpServletResponse resp = (HttpServletResponse) servletResponse;

		if (isPublic(req)) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		HttpSession session = req.getSession(false);
		boolean logged = session != null 
				&& session.getAttribute(LoginController.SESSION_ATTRIBUTE) != null;

		if (logged) {
			resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			resp.setHeader("Pragma", "no-cache");
			resp.setDateHeader("Expires", 0);

			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			resp.sendRedirect(req.getContextPath() + "/login");
		}
	}

	private boolean isPublic(HttpServletRequest req) {
		String path = req.getServletPath();

		if (path.equals("/login"))
			return true;

		for (String prefix : PUBLIC_PREFIXES) {
			if (path.startsWith(prefix))
				return true;
		}

		return false;
	}

	@Override
	public void destroy() {

	}
}
