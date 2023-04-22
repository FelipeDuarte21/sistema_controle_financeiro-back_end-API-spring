package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller.exception.ObjectBadRequestException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller.exception.ObjectNotFoundException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.UsuarioDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.UsuarioDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.UsuarioService;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.IllegalParameterException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;

@RestController
@RequestMapping(value = "/api/v1/usuario")
public class UsuarioController {
	
	private UsuarioService service;
	
	@Autowired
	public UsuarioController(UsuarioService service) {
		this.service = service;
	}
	
	@PostMapping()
	public ResponseEntity<UsuarioDTO> salvar(@RequestBody @Valid UsuarioDadosDTO usuarioDTO,
			UriComponentsBuilder uriBuilder){
		
		try {
			
			var usuario = this.service.cadastrar(usuarioDTO);
			
			var uri = uriBuilder.path("/api/v1/usuario/{id}").buildAndExpand(usuario.getId()).toUri();
			
			return ResponseEntity.created(uri).body(usuario);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
			
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<UsuarioDTO> atualizar(@PathVariable(name = "id") Long id, 
			@RequestBody @Valid UsuarioDadosDTO usuarioDTO){
		
		try {
			
			var usuario = this.service.atualizar(id, usuarioDTO);
			
			return ResponseEntity.ok(usuario);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch(ObjectNotFoundFromParameterException e){
			throw new ObjectNotFoundException(e.getMessage());
		
		}
	
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		try {
			
			var usuario = this.service.buscarPorId(id);
			
			return ResponseEntity.ok(usuario);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}

}
