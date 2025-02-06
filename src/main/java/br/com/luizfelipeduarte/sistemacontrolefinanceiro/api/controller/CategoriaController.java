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
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.CategoriaDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.CategoriaDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.CategoriaPorcentagemDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.PorcentagemDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.CategoriaService;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.IllegalParameterException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;

@RestController
@RequestMapping("api/v1/categoria")
public class CategoriaController {
	
	private CategoriaService service;
	
	@Autowired
	public CategoriaController(CategoriaService service) {
		this.service = service;
	}
	
	@PostMapping()
	public ResponseEntity<CategoriaDTO> salvar(@RequestBody @Valid CategoriaDadosDTO categoriaDadosDTO,
			UriComponentsBuilder uriBuilder){
		
		try {
			
			var categoria = this.service.salvar(categoriaDadosDTO);
			
			var uri = uriBuilder.path("api/v1/categorias/{id}")
					.buildAndExpand(categoria.getId()).toUri();
			
			return ResponseEntity.created(uri).body(categoria);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
		
		}
		
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoriaDTO> atualizar(@PathVariable(name = "id") Long id, 
			@RequestBody @Valid CategoriaDadosDTO categoriaDTO){
		
		try {
			
			var categoria = this.service.atualizar(id,categoriaDTO);
			
			return ResponseEntity.ok(categoria);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		try {
			
			var categoria = this.service.buscarPorId(id);
		
			return ResponseEntity.ok(categoria);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@GetMapping()
	public ResponseEntity<List<CategoriaDTO>> listar(@RequestParam(name = "conta") Long idConta){
		
		try {
			
			List<CategoriaDTO> categorias = this.service.listar(idConta);
			
			return ResponseEntity.ok(categorias);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@PatchMapping(value = "/porcentagem")
	public ResponseEntity<List<CategoriaDTO>> atualizarPorcentagens(@RequestBody @Valid PorcentagemDTO porcentagem){
		
		try {
			
			List<CategoriaDTO> categorias = this.service.atualizarPorcentagem(porcentagem);
			
			return ResponseEntity.ok(categorias);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
}
