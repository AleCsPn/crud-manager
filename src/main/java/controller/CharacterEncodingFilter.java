package controller;
import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

@WebFilter(urlPatterns="/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        // Obs.: a linha original servletResponse.setContentType("charset=UTF-8") foi 
        // removida. "charset=UTF-8" não é um tipo MIME válido: ela sobrescrevia o 
        // Content-Type de TODA resposta, inclusive css/js, e em versões recentes do 
        // Tomcat o navegador deixava de aplicar o CSS. Cada JSP já define seu 
        // contentType (text/html; charset=UTF-8) e o Tomcat define o dos arquivos estáticos.
        servletResponse.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
