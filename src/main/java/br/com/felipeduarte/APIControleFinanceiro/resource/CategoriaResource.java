package br.com.felipeduarte.APIControleFinanceiro.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import br.com.felipeduarte.APIControleFinanceiro.model.dto.CategoriaDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.CategoriaSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectNotFoundException;
import br.com.felipeduarte.APIControleFinanceiro.service.CategoriaService;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

@RestController
@RequestMapping("/categoria")
public class CategoriaResource {
	
	private CategoriaService service;
	
	@Autowired
	public CategoriaResource(CategoriaService service) {
		this.service = service;
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping
	public ResponseEntity<CategoriaDTO> salvar(@RequestBody @Valid CategoriaSalvarDTO categoriaDTO,
			UriComponentsBuilder uriBuilder){
		
		try {
			
			var categoria = this.service.salvar(categoriaDTO);
			
			var uri = uriBuilder.path("/categoria/{id}").buildAndExpand(categoria.getId()).toUri();
			
			return ResponseEntity.created(uri).body(categoria);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
		
		}
		
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping("/{id}")
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
	
	@PreAuthorize("hasAnyRole('USER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping("/{id}")
	public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		try {
			
			var categoria = this.service.buscarPorId(id);
		
			return ResponseEntity.ok(categoria);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping
	public ResponseEntity<Page<CategoriaDTO>> listar(
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "6") Integer size,
		@RequestParam(defaultValue = "1") Integer order
		){
		
		try {
			
			var pageCategorias = this.service.listar(page, size, order);
			
			return ResponseEntity.ok(pageCategorias);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
}
