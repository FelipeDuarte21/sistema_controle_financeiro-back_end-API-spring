package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller.exception.ObjectBadRequestException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller.exception.ObjectNotFoundException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoFixoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoFixoDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.LancamentoFixoService;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.IllegalParameterException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;

@RestController
@RequestMapping("/api/v1/lancamento-fixo")
public class LancamentoFixoController {

	private LancamentoFixoService service;
	
	@Autowired
	public LancamentoFixoController(LancamentoFixoService service) {
		this.service = service;
	}
	
	@PostMapping()
	public ResponseEntity<LancamentoFixoDTO> cadastrar(@RequestParam(name="categoria") Long idCategoria, 
			@RequestBody @Valid LancamentoFixoDadosDTO lancamentoDTO, 
			UriComponentsBuilder uriBuilder){
		
		try {
			
			var lancamentoFixo = this.service.cadastrar(idCategoria, lancamentoDTO);
			
			var uri = uriBuilder.path("api/v1/lancamento-fixo/{id}")
					.buildAndExpand(lancamentoFixo.getId()).toUri();
			
			return ResponseEntity.created(uri).body(lancamentoFixo);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<LancamentoFixoDTO> alterar(@PathVariable(name = "id") Long id, 
			@RequestBody @Valid LancamentoFixoDadosDTO lancamentoDTO){
		
		try {
			
			var lancamentoFixo = this.service.atualizar(id, lancamentoDTO);
			
			return ResponseEntity.ok(lancamentoFixo);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}catch (IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<LancamentoFixoDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		try {
			
			var lancamentoFixo = this.service.buscarPorId(id);
			
			return ResponseEntity.ok(lancamentoFixo);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@GetMapping
	public ResponseEntity<List<LancamentoFixoDTO>> listar(@RequestParam(name = "categoria") Long idCategoria){
		
		try {
			
			var lancamentosFixos = this.service.listar(idCategoria);
			
			return ResponseEntity.ok(lancamentosFixos);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}

}
