package br.com.felipeduarte.APIControleFinanceiro.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectNotContentException;
import br.com.felipeduarte.APIControleFinanceiro.service.BalancoService;

@CrossOrigin
@RestController
@RequestMapping("/balanco")
public class BalancoResource {
	
	@Autowired
	private BalancoService service;
	
	@GetMapping("/atual")
	public ResponseEntity<Balanco> recuperarAtual(@RequestParam(name = "categoria") Long idCategoria){
		
		Balanco balanco = this.service.recuperarAtual(idCategoria);
		
		if(balanco == null) {
			throw new ObjectNotContentException("Erro! Balanco não encontrado, "
					+ "verifique o id da categoria informada!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(balanco);
		
	}
	
	@GetMapping("/data")
	public ResponseEntity<Balanco> recuperarPorDate(
		@RequestParam(name = "categoria") Long idCategoria, 
		@RequestParam Integer mes,
		@RequestParam Integer ano){
		
		Balanco balanco = this.service.recuperarPorData(idCategoria, mes, ano);
		
		if(balanco == null) {
			throw new ObjectNotContentException("Erro! Balanco não encontrado, "
					+ "verifique o id da categoria informada!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(balanco);
		
	}
	
}
