package br.com.felipeduarte.APIControleFinanceiro.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.felipeduarte.APIControleFinanceiro.model.TipoLancamento;
import br.com.felipeduarte.APIControleFinanceiro.service.TipoLancamentoService;

@RestController
@RequestMapping("/tipoLancamento")
public class TipoLancamentoResource {
	
	@Autowired
	private TipoLancamentoService service;
	
	@GetMapping
	public ResponseEntity<List<TipoLancamento>> listar(){
		return null;
	}
	
}
