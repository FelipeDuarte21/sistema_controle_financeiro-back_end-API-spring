package br.com.felipeduarte.APIControleFinanceiro.resource;

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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/usuarios")
public class UsuarioResource {
	
	private UsuarioService service;
	
	@Autowired
	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}
	
	@ApiOperation(value = "Cadastra um novo usuário")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Usuário cadastrado com sucesso"),
			@ApiResponse(code = 400, message = "Usuário já cadastrado")
	})
	@PostMapping(produces = "application/json",consumes = "application/json")
	public ResponseEntity<UsuarioDTO> salvar(@RequestBody @Valid UsuarioSalvarDTO usuarioDTO,
			UriComponentsBuilder uriBuilder){
		
		try {
			
			var usuario = this.service.salvar(usuarioDTO);
			
			var uri = uriBuilder.path("api/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
			
			return ResponseEntity.created(uri).body(usuario);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
			
	}
	
	@ApiOperation(value = "Atualiza os dados de um usuário")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuário atualizado com sucesso"),
			@ApiResponse(code = 400, message = "Erro de Parâmetros ou usuário já cadastrado"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Usuário não encontrado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<UsuarioDTO> update(@PathVariable(name = "id") Long id, 
			@RequestBody @Valid UsuarioSalvarDTO usuarioDTO){
		
		try {
			
			var usuario = this.service.atualizar(id, usuarioDTO);
			
			return ResponseEntity.ok(usuario);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Exclui um usuário")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Exclui cadastro de usuário"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso Negado"),
			@ApiResponse(code = 404, message = "Usuário não encontrado")
	})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch(ObjectNotFoundFromParameterException e){
			throw new ObjectNotFoundException(e.getMessage());
		
		}
	
	}
	
	@ApiOperation(value = "Busca o usuário pela identificação")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retona usuário"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Usuário não encontrado")
	})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		try {
			
			var usuario = this.service.buscarPorId(id);
			
			return ResponseEntity.ok(usuario);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Busca um usuário pelo email")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retona usuário"),
			@ApiResponse(code = 400, message = "Erro no parametro email"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Usuário não encontrado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(value = "/email/{valor}", produces = "application/json")
	public ResponseEntity<UsuarioDTO> buscarPorEmail(@PathVariable(name = "valor") String email){
		
		try {
			
			var usuario = this.service.buscarPorEmail(email,true);
			
			return ResponseEntity.ok(usuario);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}

	@ApiOperation(value = "Busca uma página de usuários")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retona página de usuário"),
			@ApiResponse(code = 400, message = "Erro nos parâmetros"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
	})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping(produces = "application/json")
	public ResponseEntity<Page<UsuarioDTO>> listar(
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "6") Integer size,
		@RequestParam(defaultValue = "1") Integer order
		){
		
		try {
		
			var usuarios = this.service.listar(page, size, order);
			
			return ResponseEntity.ok(usuarios);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
}
