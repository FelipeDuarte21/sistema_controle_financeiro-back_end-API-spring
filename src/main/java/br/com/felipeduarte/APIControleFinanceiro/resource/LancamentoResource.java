package br.com.felipeduarte.APIControleFinanceiro.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.TransferenciaDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectNotFoundException;
import br.com.felipeduarte.APIControleFinanceiro.service.LancamentoService;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/categorias/{idCategoria}/balancos/{idBalanco}/lancamentos")
public class LancamentoResource {
	
	private LancamentoService service;
	
	@Autowired
	public LancamentoResource(LancamentoService service) {
		this.service = service;
	}
	
	@ApiOperation(value = "Cadastra um novo lançamento")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Lançamento cadastrado com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos parâmetros recebidos"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<LancamentoDTO> salvar(@PathVariable(name = "idCategoria") Long idCategoria,
			@PathVariable(name = "idBalanco") Long idBalanco, 
			@RequestBody @Valid LancamentoSalvarDTO lancamentoDTO, UriComponentsBuilder uriBuilder){
		
		try {
			
			var lancamento = this.service.salvar(idBalanco,lancamentoDTO);
			
			var uri = uriBuilder.path(
					"api/categorias/{idCategoria}/balancos/{idBalanco}/lancamentos/{id}")
					.buildAndExpand(idCategoria,idBalanco, lancamento.getId()).toUri();
			
			return ResponseEntity.created(uri).body(lancamento);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Atualiza os dados do lançamento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lançamento atualizado com sucesso"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<LancamentoDTO> atualizar(@PathVariable("id") Long id, 
			@RequestBody @Valid LancamentoSalvarDTO lancamentoDTO){
		
		try {
			
			var lancamento = this.service.alterar(id,lancamentoDTO);
			
			return ResponseEntity.ok(lancamento);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Exclui lançamento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lançamento excluído com sucesso"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Lançamento não encontrado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Busca o lançamento pela identificação")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lançamento encontrado"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Lançamento não encontrado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<LancamentoDTO> buscarPorId(@PathVariable Long id){
		
		try {
			
			var lancamento = this.service.buscarPorId(id);
			
			return ResponseEntity.ok(lancamento);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Obtem uma página de lançamento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Página retorna com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos paramêtros recebidos"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Balanço não encontrado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(produces = "application/json")
	public ResponseEntity<Page<LancamentoDTO>> listar(
			@PathVariable(name = "idBalanco") Long idBalanco,
			@PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "dataLancamento") Pageable paginacao
		){
		
		try {
			
			var pageLancamentos = this.service.listar(idBalanco, paginacao);
		
			return ResponseEntity.ok(pageLancamentos);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Obtem um arquivo csv dos lançamentos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo obtido com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos geração do arquivo csv"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(value = "/arquivo", produces = "text/csv")
	public ResponseEntity<Resource> gerarArquivoCSV(@PathVariable(name = "idBalanco") Long idBalanco){
		
		try {
			
			var arquivoDTO = this.service.gerarArquivoCSV(idBalanco);
			
		    var headers = new HttpHeaders();
		    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + arquivoDTO.getNomeArquivo());
		    headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
		    headers.add("file_name", arquivoDTO.getNomeArquivo());

		    return new ResponseEntity<>(arquivoDTO.getArquivo(),headers,HttpStatus.OK);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Transferência entre categorias")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Transferência feita com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos paramêtros recebidos"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping("/transferencia")
	public ResponseEntity<?> transferir(@RequestBody @Valid TransferenciaDTO transferencia){
		
		try {
			
			this.service.tranferir(transferencia);
			
			return ResponseEntity.ok().build();
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
}
