package br.com.felipeduarte.APIControleFinanceiro.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.CategoriaDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.service.CategoriaService;

@RestController
@RequestMapping("/categoria")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@PostMapping
	public ResponseEntity<Categoria> salvar(@RequestBody @Valid CategoriaDTO categoria){
		
		Categoria cat = this.service.salvar(categoria);
		
		if(cat == null) {
			throw new ObjectBadRequestException("Categória já cadastrada!");
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(cat);
		
	}
	
	@PutMapping
	public ResponseEntity<Categoria> atualizar(@RequestBody @Valid CategoriaDTO categoria){
		
		Categoria cat = this.service.atualizar(categoria);
		
		if(cat == null) {
			throw new ObjectBadRequestException("Erro! Verifique o id informado!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(cat);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		boolean resp = this.service.excluir(id);
		
		if(resp == false) {
			throw new ObjectBadRequestException("Erro! Verifique o id informado!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping
	public ResponseEntity<Page<Categoria>> listar(
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "6") Integer size,
		@RequestParam(defaultValue = "1") Integer order
		){
		
		Page<Categoria> categorias = this.service.listar(page, size, order);
		
		return ResponseEntity.status(HttpStatus.OK).body(categorias);
		
	}
	
}
