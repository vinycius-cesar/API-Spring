/**
 * 
 */
package spring.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import spring.api.rest.ApplicationContextLoad;
import spring.api.rest.model.Usuario;
import spring.api.rest.repository.UsuarioRepository;

/**
 * @author Vinycius
 *
 */
@Service
@Component
public class JWTTokenAutenticacaoService {

	//Tempo de validade do Token = 2 dias
	private static final long EXPIRATION_TIME = 172800000;
	
	//Uma senha unica para compor a autenticacao e ajudar na seguranca
	private static final String SECRET = "SenhaExtramamenteSecreta";
	
	//Prefixo padrão de Token
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	
	//Gerando token de autenticacao e adicionando o cabeçalho e resposta http
	public void addAuthentication(HttpServletResponse response, String username) throws IOException{
		//Montagem do Token
		String JWT = Jwts.builder()//Chama o gerador de Toker
				.setSubject(username)//Adiciona o usuario 
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))// tempo de expiracao do Token
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();//Compactacao e algoritmos de geracao de senha
		
		//Junta o token com o prefixo
		String token = TOKEN_PREFIX + " " + JWT; //ex: Bearer 932984923849238
		
		//Adiciona no cabeçalho http
		response.addHeader(HEADER_STRING, token);// authorization: bearer 81231283718237813
		
		//Escreve token como resposta no corpo do http
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
	}
	
	
	//Retorna o usuario validado com o token ou caso nao seja valido retorna null
	public Authentication getAuthentication(HttpServletRequest request) {
		
		//Pega o token enviado no cabeçalho http
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null) {
			//Faz a validacao do Token do usuario na requisicao
			String user = Jwts.parser().setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody().getSubject();
			
			if(user != null) {
				Usuario usuario = ApplicationContextLoad.getApplicationContext()
						.getBean(UsuarioRepository.class).findUserByLogin(user);
				
				if(usuario != null) {
					
					return new UsernamePasswordAuthenticationToken(
							usuario.getLogin(), 
							usuario.getSenha(),
							usuario.getAuthorities());
				}
			}
			
		}
			return null; // não autorizado
		
	}
	
}
