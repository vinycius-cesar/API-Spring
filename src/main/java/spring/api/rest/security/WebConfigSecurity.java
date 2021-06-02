/**
 * 
 */
package spring.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import spring.api.rest.service.ImplementacaoUserDetailsService;

/**
 * @author Vinycius
 *
 */
/*Mapeia URL, enderecos, autoriza ou bloqueia acessos a URL*/
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	//Configura as solicitacoes de acesso por Http
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Ativando a proteção contra o usuario que não estao validados por token
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		//Ativando a permissão para acesso a pagina inicial do sistema EX: sistema.combr/index.html
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()
		
		//URL de logout - redireciona após o user deslogar do sistema
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		//Mapeia URL de logou e invalida o usuario
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		
		//Filtra requisiçoes de login para autenticacao
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
		
		//Filtra demais requisiçoes para verificar a presença do TOKEN JWT no HEADER HTTP
		.addFilterBefore(new JWTApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//Service que irá consultar o usuario no banco de dados
		auth.userDetailsService(implementacaoUserDetailsService).
		//padrao de condificao de senha
		passwordEncoder(new BCryptPasswordEncoder());
	}
}
