package br.com.felipeduarte.APIControleFinanceiro.resource;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.UsuarioAtualizarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.UsuarioDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.UsuarioSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectNotFoundException;
import br.com.felipeduarte.APIControleFinanceiro.service.UsuarioService;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.NotFoundObjectToParameterException;

@RestController
@RequestMapping("/usuario")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService service;
	
	@PostMapping
	public ResponseEntity<Usuario> salvar(@RequestBody @Valid UsuarioSalvarDTO usuario){
		
		Usuario usu = this.service.salvar(usuario);
		
		if(usu == null) {
			throw new ObjectBadRequestException("Usuário Já Cadastrado!");
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(usu);
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping
	public ResponseEntity<Usuario> update(@RequestBody @Valid UsuarioAtualizarDTO usuario){
		
		Usuario usu = this.service.atualizar(usuario);
		
		if(usu == null) {	
			throw new ObjectBadRequestException("Erro! Verifique o id informado, "
					+ "verifique se o usuario está cadastrado e verifique os dados informados!");		
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(usu);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch(NotFoundObjectToParameterException e){
			
			throw new ObjectNotFoundException(e.getMessage());
		
		}
	
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		Optional<UsuarioDTO> optUsuario = this.service.buscarPorId(id);
		
		if(!optUsuario.isPresent())
			throw new ObjectNotFoundException("Usuário não encontrado!");
		
		return ResponseEntity.ok(optUsuario.get());
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping("/email")
	public ResponseEntity<UsuarioDTO> buscarPorEmail(@RequestParam String email){
		
		Optional<UsuarioDTO> optUsuario = this.service.buscarPorEmail(email,true);
		
		return ResponseEntity.ok(optUsuario.get());
		
	}

	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping
	public ResponseEntity<Page<UsuarioDTO>> listar(
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "6") Integer size,
		@RequestParam(defaultValue = "1") Integer order
		){
		
		Page<UsuarioDTO> usuarios = this.service.listar(page, size, order);
		
		return ResponseEntity.ok(usuarios);
		
	}
	
}
