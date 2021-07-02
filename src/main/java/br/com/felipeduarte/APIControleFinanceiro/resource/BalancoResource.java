package br.com.felipeduarte.APIControleFinanceiro.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.service.BalancoService;

@RestController
@RequestMapping("/balanco")
public class BalancoResource {
	
	@Autowired
	private BalancoService service;
	
	
	public ResponseEntity<Balanco> recuperarAtual(Long idCategoria){
		return null;
	}
	
	public ResponseEntity<Balanco> recuperarPorDate(Integer mes,Integer ano, Long idCategoria){
		return null;
	}
	
}
