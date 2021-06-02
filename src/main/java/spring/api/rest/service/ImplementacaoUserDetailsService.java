/**
 * 
 */
package spring.api.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import spring.api.rest.model.Usuario;
import spring.api.rest.repository.UsuarioRepository;

/**
 * @author Vinycius
 *
 */
@Service
public class ImplementacaoUserDetailsService implements UserDetailsService {
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*CONSULTAR NO BANCO O USUARIO*/
		Usuario usuario = usuarioRepository.findUserByLogin(username);
		
		if(usuario == null) {
			throw new UsernameNotFoundException("usuaio nao encontrado");
		}
		
		return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());
	}

}
