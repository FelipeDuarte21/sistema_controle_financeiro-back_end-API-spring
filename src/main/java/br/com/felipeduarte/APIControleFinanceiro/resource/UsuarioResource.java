package br.com.felipeduarte.APIControleFinanceiro.resource;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.UsuarioDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.UsuarioSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectNotFoundException;
import br.com.felipeduarte.APIControleFinanceiro.service.UsuarioService;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

@RestController
@RequestMapping("/usuario")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService service;
	
	@PostMapping
	public ResponseEntity<UsuarioDTO> salvar(@RequestBody @Valid UsuarioSalvarDTO usuarioDTO,
			UriComponentsBuilder uriBuilder){
		
		try {
			
			UsuarioDTO usuario = this.service.salvar(usuarioDTO);
			
			URI uri = uriBuilder.path("/usuario/{id}").buildAndExpand(usuario.getId()).toUri();
			
			return ResponseEntity.created(uri).body(usuario);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
			
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioDTO> update(@PathVariable(name = "id") Long id, 
			@RequestBody @Valid UsuarioSalvarDTO usuarioDTO){
		
		try {
			
			UsuarioDTO usuario = this.service.atualizar(id, usuarioDTO);
			
			return ResponseEntity.ok(usuario);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch(ObjectNotFoundFromParameterException e){
			throw new ObjectNotFoundException(e.getMessage());
		
		}
	
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		try {
			
			UsuarioDTO usuario = this.service.buscarPorId(id);
			
			return ResponseEntity.ok(usuario);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping("/email")
	public ResponseEntity<UsuarioDTO> buscarPorEmail(@RequestParam String email){
		
		try {
			
			UsuarioDTO usuario = this.service.buscarPorEmail(email,true);
			
			return ResponseEntity.ok(usuario);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}

	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping
	public ResponseEntity<Page<UsuarioDTO>> listar(
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "6") Integer size,
		@RequestParam(defaultValue = "1") Integer order
		){
		
		try {
		
			Page<UsuarioDTO> usuarios = this.service.listar(page, size, order);
			
			return ResponseEntity.ok(usuarios);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
}
