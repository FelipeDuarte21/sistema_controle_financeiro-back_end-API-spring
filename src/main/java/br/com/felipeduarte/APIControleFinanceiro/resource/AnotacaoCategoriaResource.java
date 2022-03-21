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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.AnotacaoCategoriaDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.AnotacaoCategoriaSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectNotFoundException;
import br.com.felipeduarte.APIControleFinanceiro.service.AnotacaoCategoriaService;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/categorias/{idCategoria}/anotacoes")
public class AnotacaoCategoriaResource {
	
	private AnotacaoCategoriaService service;
	
	@Autowired
	public AnotacaoCategoriaResource(AnotacaoCategoriaService anotacaoCategoriaService) {
		this.service = anotacaoCategoriaService;
	}
	
	@ApiOperation(value = "Cadastra uma nova antoação da categoria")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Anotação criada com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos parametros informados não encontrada"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<AnotacaoCategoriaDTO> salvar(@PathVariable(name = "idCategoria") Long idCategoria, 
			@RequestBody @Valid AnotacaoCategoriaSalvarDTO anotacaoDTO, UriComponentsBuilder uriBuilder) {
		
		try {
			
			var anotacao = this.service.salvar(idCategoria, anotacaoDTO);
			
			var uri = uriBuilder.path("api/categorias/{idCategoria}/anotacoes/{id}")
					.buildAndExpand(anotacao.getCategoria().getId(),anotacao.getId()).toUri();
			
			return ResponseEntity.created(uri).body(anotacao);
			
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Atualiza a anotação da categoria")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Anotação atualizada com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos parametros informados não encontrada"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Anotação não encontrada")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping(value = "/{id}",consumes = "application/json", produces = "application/json")
	public ResponseEntity<AnotacaoCategoriaDTO> atualizar(
			@PathVariable(name = "idCategoria")Long idCategoria, @PathVariable(name = "id") Long id, 
			@RequestBody @Valid AnotacaoCategoriaSalvarDTO anotacaoDTO) {
		
		try {
			
			var anotacao = this.service.atualizar(idCategoria, id, anotacaoDTO);
			
			return ResponseEntity.ok(anotacao);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	
	@ApiOperation(value = "Atualiza o atributo riscado da anotação")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Anotação atualizada com sucesso"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Anotação não encontrada")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PatchMapping(value = "/riscado/{id}", produces = "application/json")
	public ResponseEntity<AnotacaoCategoriaDTO> riscar(@PathVariable(name = "id") Long id){
		
		try {
			
			var anotacao = this.service.riscar(id);
			
			return ResponseEntity.ok(anotacao);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}

	@ApiOperation(value = "Exclui a anotação da categoria")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Anotação excluída com sucesso"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Anotação não encontrada")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id) {
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}

	@ApiOperation(value = "Busca a anotação pela identificação")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Anotação retornada com sucesso"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Anotação não encontrada")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<AnotacaoCategoriaDTO> buscarPorId(@PathVariable(name = "id") Long id) {
		
		try {
			
			var anotacao = this.service.buscarPorId(id);
			
			return ResponseEntity.ok(anotacao);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Busca uma página de anotação da categoria")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Página de anotações retornada com sucesso"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Categoria não encontrada")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(produces = "application/json")
	public ResponseEntity<Page<AnotacaoCategoriaDTO>> listar(
			@PathVariable(name = "idCategoria") Long idCategoria, 
			@PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "data") Pageable paginacao) {
		
		try {
			
			var pagAnotacoes = this.service.listar(idCategoria, paginacao);
			
			return ResponseEntity.ok(pagAnotacoes);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}

}
