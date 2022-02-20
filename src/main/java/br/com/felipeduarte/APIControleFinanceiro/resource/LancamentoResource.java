package br.com.felipeduarte.APIControleFinanceiro.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
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

@RestController
@RequestMapping("api/categorias/{idCategoria}/balancos/{idBalanco}/lancamentos")
public class LancamentoResource {
	
	private LancamentoService service;
	
	@Autowired
	public LancamentoResource(LancamentoService service) {
		this.service = service;
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping
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
	
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping("/{id}")
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
	
	@PreAuthorize("hasAnyRole('USER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping("/{id}")
	public ResponseEntity<LancamentoDTO> buscarPorId(@PathVariable Long id){
		
		try {
			
			var lancamento = this.service.buscarPorId(id);
			
			return ResponseEntity.ok(lancamento);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping
	public ResponseEntity<Page<LancamentoDTO>> listar(
			@PathVariable(name = "idBalanco") Long idBalanco,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "6") Integer size,
			@RequestParam(defaultValue = "2") Integer order
		){
		
		try {
			
			Page<LancamentoDTO> lancamentos = this.service.listar(idBalanco, page, size, order);
		
			return ResponseEntity.ok(lancamentos);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
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
