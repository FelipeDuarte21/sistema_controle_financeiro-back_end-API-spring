package br.com.felipeduarte.APIControleFinanceiro.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.BalancoDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.BalancoFaixaDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.service.BalancoService;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;

@RestController
@RequestMapping("/balanco")
public class BalancoResource {
	
	@Autowired
	private BalancoService service;
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping("/resumo")
	public ResponseEntity<List<BalancoFaixaDTO>> buscarFaixas(
			@RequestParam(name = "categoria") Long idCategoria,
			@RequestParam(name = "ano") Integer ano, 
			@RequestParam(name = "mes") Integer mes, 
			@RequestParam(name = "qtdMes") Integer qtdMes){
		
		try {
			
			var balancos = this.service.buscarFaixas(idCategoria, ano, mes, qtdMes);
			
			return ResponseEntity.ok(balancos);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping("/atual")
	public ResponseEntity<BalancoDTO> recuperarAtual(@RequestParam(name = "categoria") Long idCategoria){
		
		try {
			
			var balanco = this.service.recuperarAtual(idCategoria);
			
			return ResponseEntity.ok(balanco);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping("/data")
	public ResponseEntity<BalancoDTO> buscarPorData(
		@RequestParam(name = "categoria") Long idCategoria, 
		@RequestParam Integer mes,
		@RequestParam Integer ano){
		
		try {
			
			var balanco = this.service.buscarPorData(idCategoria, mes, ano);
			
			return ResponseEntity.ok(balanco);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
}
