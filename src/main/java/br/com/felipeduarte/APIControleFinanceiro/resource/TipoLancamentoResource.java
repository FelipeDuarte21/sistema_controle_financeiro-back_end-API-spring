package br.com.felipeduarte.APIControleFinanceiro.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.TipoLancamentoDTO;
import br.com.felipeduarte.APIControleFinanceiro.service.TipoLancamentoService;

@RestController
@RequestMapping("api/tipos-lancamentos")
public class TipoLancamentoResource {
	
	private TipoLancamentoService service;
	
	@Autowired
	public TipoLancamentoResource(TipoLancamentoService service) {
		this.service = service;
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping
	public ResponseEntity<List<TipoLancamentoDTO>> listar(){
		
		var tipos = this.service.listar();
		
		return ResponseEntity.ok(tipos);
		
	}
	
}
