package spring.api.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.api.rest.model.Usuario;
import spring.api.rest.repository.UsuarioRepository;


@RestController //receber requisições restfull
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@Autowired 
	private UsuarioRepository usuarioRepository;
    
	
	@GetMapping(value = "/{id}/codigovenda/{venda}", produces = "application/json") //acessando pela url resgatando valor
	public ResponseEntity<Usuario> relatorio(@PathVariable (value = "id") Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		//o retorno seria um relatorio
		return new ResponseEntity(usuario.get(), HttpStatus.OK);
	}
	
	
	// é um serviço RestFull
	@GetMapping(value = "/{id}", produces = "application/json") //acessando pela url resgatando valor
	public ResponseEntity<Usuario> init(@PathVariable (value = "id") Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		return new ResponseEntity(usuario.get(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuario (){
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	
	
	//salvar usuarios
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario){
		
		for(int pos = 0; pos < usuario.getTelefones().size(); pos ++) { //o for varre a lista e amarra o telefone que veio do usuario pai
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	
	
	//atualizando utiliza o metodo salvar
		@PutMapping(value = "/", produces = "application/json")
		public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario){
			//outras rotinas antes de atualizar
			for(int pos = 0; pos < usuario.getTelefones().size(); pos ++) { //o for varre a lista e amarra o telefone que veio do usuario pai
				usuario.getTelefones().get(pos).setUsuario(usuario);
			}
			
			Usuario usuarioSalvo = usuarioRepository.save(usuario);
			
			return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
		}
		
		
		//atualizando com o id
		@PutMapping(value = "/{iduser}/idvenda/{idvenda}", produces = "application/json") //acessando pela url resgatando valor
		public ResponseEntity<Usuario> updateVenda(@PathVariable (value = "id") Long id) {
			Optional<Usuario> usuario = usuarioRepository.findById(id);
			
			//o retorno seria um relatorio
			return new ResponseEntity("venda Atualizada", HttpStatus.OK);
		}
		
		
		//metodo para deletar
		@DeleteMapping(value = "/{id}", produces = "application/text")
		public String delete(@PathVariable("id") Long id) {
			
			usuarioRepository.deleteById(id);
			
			return "ok";
		}
		
	
	
}
