package br.com.felipeduarte.APIControleFinanceiro.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.CategoriaDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.CategoriaSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectNotFoundException;
import br.com.felipeduarte.APIControleFinanceiro.service.CategoriaService;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/categorias")
public class CategoriaResource {
	
	private CategoriaService service;
	
	@Autowired
	public CategoriaResource(CategoriaService service) {
		this.service = service;
	}
	
	@ApiOperation(value = "Cadastra uma nova categoria")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Categoria cadastrada com sucesso"),
			@ApiResponse(code = 400, message = "Categoria já cadastrada"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping(produces = "application/json",consumes = "application/json")
	public ResponseEntity<CategoriaDTO> salvar(@RequestBody @Valid CategoriaSalvarDTO categoriaDTO,
			UriComponentsBuilder uriBuilder){
		
		try {
			
			var categoria = this.service.salvar(categoriaDTO);
			
			var uri = uriBuilder.path("api/categorias/{id}")
					.buildAndExpand(categoria.getId()).toUri();
			
			return ResponseEntity.created(uri).body(categoria);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
		
		}
		
	}
	
	@ApiOperation(value = "Atualiza os dados de uma categoria")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Categoria atualizada com sucesso"),
			@ApiResponse(code = 400, message = "Dados recebidos inválidos"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
	public ResponseEntity<CategoriaDTO> atualizar(@PathVariable(name = "id") Long id, 
			@RequestBody @Valid CategoriaSalvarDTO categoriaDTO){
		
		try {
			
			var categoria = this.service.atualizar(id,categoriaDTO);
			
			return ResponseEntity.ok(categoria);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Exclui uma categoria")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Categoria excluida com sucesso"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 404, message = "Categoria não encontrada"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Busca a categoria pela identificação")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Categoria encontrada"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 404, message = "Categoria não encontrada"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		try {
			
			var categoria = this.service.buscarPorId(id);
		
			return ResponseEntity.ok(categoria);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Busca uma página de categoria")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna a página"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(produces = "application/json")
	public ResponseEntity<Page<CategoriaDTO>> listar(@PageableDefault
			(page = 0, size = 10, direction = Direction.ASC, sort = "nome") Pageable paginacao
		){
			
		var pageCategorias = this.service.listar(paginacao);
			
		return ResponseEntity.ok(pageCategorias);
		
	}
	
}
