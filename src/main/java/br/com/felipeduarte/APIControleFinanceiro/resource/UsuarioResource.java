package br.com.felipeduarte.APIControleFinanceiro.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.service.UsuarioService;

@CrossOrigin
@RestController
@RequestMapping("/usuario")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService service;
	
	@PostMapping
	public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario){
		return null;
	}
	
	@PutMapping
	public ResponseEntity<Usuario> update(@RequestBody Usuario usuario){
		return null;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		return null;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> buscarPorId(@PathVariable(name = "id") Long id){
		return null;
	}
	
	@GetMapping
	public ResponseEntity<Page<Usuario>> listar(
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "6") Integer size,
		@RequestParam(defaultValue = "1") Integer order
		){
		
		Page<Usuario> usuarios = this.service.listar(page, size, order);
		
		return ResponseEntity.status(HttpStatus.OK).body(usuarios);
		
	}
	
}
