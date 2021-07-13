package br.com.felipeduarte.APIControleFinanceiro.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.felipeduarte.APIControleFinanceiro.model.TipoLancamento;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectNotContentException;
import br.com.felipeduarte.APIControleFinanceiro.service.TipoLancamentoService;

@RestController
@RequestMapping("/tipoLancamento")
public class TipoLancamentoResource {
	
	@Autowired
	private TipoLancamentoService service;
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping
	public ResponseEntity<List<TipoLancamento>> listar(){
		
		List<TipoLancamento> tipos = this.service.listar();
		
		if(tipos == null) {
			throw new ObjectNotContentException("Não há tipos cadastrados");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(tipos);
	}
	
}
