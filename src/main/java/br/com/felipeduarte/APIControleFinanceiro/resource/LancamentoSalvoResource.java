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

import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoSalvoSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoSalvoDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectNotFoundException;
import br.com.felipeduarte.APIControleFinanceiro.service.LancamentoSalvoService;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/categorias/{idCategoria}/lancamentos-salvos")
public class LancamentoSalvoResource {

	private LancamentoSalvoService service;
	
	@Autowired
	public LancamentoSalvoResource(LancamentoSalvoService service) {
		this.service = service;
	}
	
	@ApiOperation(value = "Salva um lancamento")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Lancamento salvo com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos parametros recebidos"),
			@ApiResponse(code = 401, message = "Acesso Indevido"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Categoria não encontrada"),
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<LancamentoSalvoDTO> cadastrar(
			@PathVariable(name = "idCategoria") Long idCategoria, 
			@RequestBody @Valid LancamentoSalvoSalvarDTO lancamentoDTO, 
			UriComponentsBuilder uriBuilder){
		
		try {
			
			var lancamentoSalvo = this.service.cadastrar(idCategoria, lancamentoDTO);
			
			var uri = uriBuilder.path("api/lancamentos-salvos/{id}")
					.buildAndExpand(lancamentoSalvo.getId()).toUri();
			
			return ResponseEntity.created(uri).body(lancamentoSalvo);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Altera um lancamento salvo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lancamento alterado com sucesso"),
			@ApiResponse(code = 404, message = "Lancamento não encontrado"),
			@ApiResponse(code = 401, message = "Acesso Indevido"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<LancamentoSalvoDTO> alterar(@PathVariable(name = "id") Long id, 
			@RequestBody @Valid LancamentoSalvoSalvarDTO lancamentoDTO){
		
		try {
			
			var lancamentoSalvo = this.service.atualizar(id, lancamentoDTO);
			
			return ResponseEntity.ok(lancamentoSalvo);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Exclui um lancamento salvo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lancamento excluido com sucesso"),
			@ApiResponse(code = 404, message = "Lancamento não encontrado"),
			@ApiResponse(code = 401, message = "Acesso Indevido"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Busca pela identificação um lancamento salvo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lancamento encontrado com sucesso"),
			@ApiResponse(code = 404, message = "Lancamento não encontrado"),
			@ApiResponse(code = 401, message = "Acesso Indevido"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<LancamentoSalvoDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		try {
			
			var lancamentoSalvo = this.service.buscarPorId(id);
			
			return ResponseEntity.ok(lancamentoSalvo);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Obtem uma pagina de lancamento salvo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pagina encontrada com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos parâmetros passados"),
			@ApiResponse(code = 401, message = "Acesso Indevido"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Categoria não encontrada"),
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(produces = "application/json")
	public ResponseEntity<Page<LancamentoSalvoDTO>> listar(
			@PathVariable(name = "idCategoria") Long idCategoria,
			@PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "nome") Pageable paginacao){
		
		try {
			
			var pagLancamentoSalvo = this.service.listar(idCategoria, paginacao);
			
			return ResponseEntity.ok(pagLancamentoSalvo);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}

}
