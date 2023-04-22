package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.AnotacaoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.AnotacaoDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.AnotacaoService;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;

@RestController
@RequestMapping("api/v1/anotacao")
public class AnotacaoController {
	
	private AnotacaoService service;
	
	@Autowired
	public AnotacaoController(AnotacaoService anotacaoService) {
		this.service = anotacaoService;
	}
	
	
	@PostMapping()
	public ResponseEntity<AnotacaoDTO> salvar(@RequestParam(name = "categoria") Long idCategoria, 
			@RequestBody @Valid AnotacaoDadosDTO anotacaoDTO, UriComponentsBuilder uriBuilder) {
		
		try {
			
			var anotacao = this.service.salvar(idCategoria, anotacaoDTO);
			
			var uri = uriBuilder.path("api/v1/anotacao/{id}")
					.buildAndExpand(anotacao.getId()).toUri();
			
			return ResponseEntity.created(uri).body(anotacao);
			
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<AnotacaoDTO> atualizar(@PathVariable(name = "id") Long id, 
			@RequestBody @Valid AnotacaoDadosDTO anotacaoDTO) {
		
		try {
			
			var anotacao = this.service.atualizar(id, anotacaoDTO);
			
			return ResponseEntity.ok(anotacao);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@PatchMapping(value = "{id}/riscado")
	public ResponseEntity<AnotacaoDTO> riscar(@PathVariable(name = "id") Long id){
		
		try {
			
			var anotacao = this.service.riscar(id);
			
			return ResponseEntity.ok(anotacao);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id) {
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}

	
	@GetMapping(value = "/{id}")
	public ResponseEntity<AnotacaoDTO> buscarPorId(@PathVariable(name = "id") Long id) {
		
		try {
			
			var anotacao = this.service.buscarPorId(id);
			
			return ResponseEntity.ok(anotacao);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@GetMapping()
	public ResponseEntity<List<AnotacaoDTO>> listar(@RequestParam(name = "categoria") Long idCategoria) {
		
		try {
			
			var anotacoes = this.service.listar(idCategoria);
			
			return ResponseEntity.ok(anotacoes);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}

}
