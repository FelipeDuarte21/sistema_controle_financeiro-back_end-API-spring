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
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.ParcelaDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.ParceladoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.ParceladoDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.ParceladoService;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.IllegalParameterException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;


@RestController
@RequestMapping("/api/v1/parcelado")
public class ParceladoController {
	
	public ParceladoService service;
	
	@Autowired
	public ParceladoController(ParceladoService service) {
		this.service = service;
	}
		
	@PostMapping()
	public ResponseEntity<ParceladoDTO> cadastrar(@RequestParam(name = "categoria") Long idCategoria, 
			@RequestBody @Valid ParceladoDadosDTO parceladoDTO, UriComponentsBuilder uriBuilder){
		
		try {
			
			var parcelado = this.service.cadastrar(idCategoria, parceladoDTO);
			
			var uri = uriBuilder.path("api/v1/parcelado/{id}")
					.buildAndExpand(parcelado.getId()).toUri();
			
			return ResponseEntity.created(uri).body(parcelado);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ParceladoDTO> atualizar(@PathVariable(name = "id") Long id, 
			@RequestBody @Valid ParceladoDadosDTO parceladoDTO){
		
		try {
			
			var parcelado = this.service.alterar(id, parceladoDTO);
			
			return ResponseEntity.ok(parcelado);
			
		}catch (IllegalParameterException  ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
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
	public ResponseEntity<ParceladoDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		try {
			
			var parcelado = this.service.buscarPorId(id);
			
			return ResponseEntity.ok(parcelado);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
		
	}
	
	@GetMapping
	public ResponseEntity<List<ParceladoDTO>> listar(@RequestParam(name = "categoria") Long idCategoria){
		
		try {
			
			var parcelados = this.service.listar(idCategoria);
			
			return ResponseEntity.ok(parcelados);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@GetMapping(value = "{idParcelado}/parcelas")
	public ResponseEntity<List<ParcelaDTO>> listarParcelas(
			@PathVariable(name = "idParcelado") Long idParcelado){
		
		try {
			
			var parcelas = this.service.listarParcelas(idParcelado);
			
			return ResponseEntity.ok(parcelas);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
		
	}
	
	@GetMapping(value = "{idParcelado}/parcelas-nao-pagas")
	public ResponseEntity<List<ParcelaDTO>> listarParcelasNaoPagas(
			@PathVariable(name = "idParcelado") Long idParcelado){
		
		try {
			
			var parcelas = this.service.listarParcelasNaoPagas(idParcelado);
			
			return ResponseEntity.ok(parcelas);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
		
	}
	
	
	@GetMapping(value = "/nao-quitados")
	public ResponseEntity<List<ParceladoDTO>> listarNaoQuitados(
			@RequestParam(name = "categoria") Long idCategoria){
		
		try {
			
			var parcelados = this.service.listarNaoQuitados(idCategoria);
			
			return ResponseEntity.ok(parcelados);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
		
	}
	
	

}
