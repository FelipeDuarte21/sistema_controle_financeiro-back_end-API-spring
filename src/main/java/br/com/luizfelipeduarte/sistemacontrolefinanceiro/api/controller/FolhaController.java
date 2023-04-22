package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller.exception.ObjectBadRequestException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller.exception.ObjectNotFoundException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.FolhaDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.ParcelaPagamentoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.TransferenciaDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.FolhaService;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.IllegalParameterException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;

@RestController
@RequestMapping("api/v1/folha")
public class FolhaController {
	
	private FolhaService service;
	
	@Autowired
	public FolhaController(FolhaService service) {
		this.service = service;
	}
	
	@GetMapping(value = "/atual")
	public ResponseEntity<FolhaDTO> recuperarAtual(@RequestParam(name = "categoria") Long idCategoria){
		
		try {
			
			var folha = this.service.recuperarAtual(idCategoria);
			
			return ResponseEntity.ok(folha);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@GetMapping
	public ResponseEntity<List<FolhaDTO>> listar(@RequestParam(name = "categoria")Long idCategoria){
		
		try {
			
			var folhas = this.service.listar(idCategoria);
			
			return ResponseEntity.ok(folhas);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@PostMapping(value = "/lancamento")
	public ResponseEntity<LancamentoDTO> fazerLancamento(@RequestParam(name = "folha")Long idFolha,
			@RequestBody @Valid LancamentoDadosDTO lancamentoDadosDTO,UriComponentsBuilder uriBuilder){
		
		try {
			
			var lancamento = this.service.fazerLancamento(idFolha, lancamentoDadosDTO);
			
			var uri = uriBuilder.path("api/v1/folha/lancamento/{id}?folha={idFolha}")
					.buildAndExpand(lancamento.getId(),idFolha).toUri();
			
			
			return ResponseEntity.created(uri).body(lancamento);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	
	@PutMapping(value = "/lancamento/{id}")
	public ResponseEntity<LancamentoDTO> alterarLancamento(@RequestParam(name = "folha") Long idFolha,
			@PathVariable(name = "id") Long idLancamento,
			@RequestBody @Valid LancamentoDadosDTO lancamentoDadosDTO){
		
		try {
			
			var lancamento = this.service.alterarLancamento(idFolha, idLancamento, lancamentoDadosDTO);
			
			return ResponseEntity.ok(lancamento);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@DeleteMapping("/lancamento/{id}")
	public ResponseEntity<?> excluirLancamento(@RequestParam(name = "folha") Long idFolha,
			@PathVariable(name = "id") Long idLancamento){
		
		try {
			
			this.service.excluirLancamento(idFolha, idLancamento);
			
			return ResponseEntity.ok().build();
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	
	@PostMapping(value = "/lancamento-parcelado")
	public ResponseEntity<LancamentoDTO> fazerLancamentoParcelado(
			@RequestParam(value="folha") Long idFolha, @RequestParam(value="parcelado") Long idParcelado, 
			@RequestParam(value="parcela") Long idParcela,
			@RequestBody @Valid ParcelaPagamentoDTO parcelaPagamentoDTO,
			UriComponentsBuilder uriBuilder){
		
		try {
			
			var lancamento = this.service.fazerLancamentoParcelado(idFolha, idParcelado, idParcela, parcelaPagamentoDTO);
			
			var uri = uriBuilder.path("api/v1/folha/lancamento/{id}?folha={idFolha}")
					.buildAndExpand(lancamento.getId(),idFolha).toUri();
			
			return ResponseEntity.created(uri).body(lancamento);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@PostMapping(value = "/lancamento-todas-categorias")
	public ResponseEntity<?> fazerLancamentoTodasCategorias(@RequestParam(name="conta") Long idConta){
		
		try {
			
			this.service.fazerLancamentosParaTodasCategorias(idConta);
			
			return ResponseEntity.ok().build();
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@PostMapping(value = "/transferencia")
	public ResponseEntity<?> fazerTransferência(@RequestBody @Valid TransferenciaDTO transferenciaDTO){
		
		try {
			
			this.service.fazerTransferência(transferenciaDTO);
			
			return ResponseEntity.ok().build();
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@GetMapping(value = "/arquivo",produces = "text/csv")
	public ResponseEntity<Resource> gerarArquivoCSV(@RequestParam(name="folha") Long idFolha){
		
		try {
			
			var arquivoDTO = this.service.gerarArquivoCSV(idFolha);
			
			var headers = new HttpHeaders();
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + arquivoDTO.getNomeArquivo());
			headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
			headers.add("file_name", arquivoDTO.getNomeArquivo());

			return new ResponseEntity<>(arquivoDTO.getArquivo(),headers,HttpStatus.OK);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
}
