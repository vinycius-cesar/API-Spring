/**
 * 
 */
package spring.api.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author Vinycius
 *
 */
//filtro onde todas as requisições serao capturadas para autenticar
public class JWTApiAutenticacaoFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//Estabelece a autenticacao para a requisição
		Authentication authentication = new JWTTokenAutenticacaoService().getAuthentication((HttpServletRequest)request);
		
		//Coloca o processo de autenticacao no spring security
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		//Continua o processo
		chain.doFilter(request, response);
		
	}

}
