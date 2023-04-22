package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller.exception.ObjectNotFoundException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.LancamentoService;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;

@RestController
@RequestMapping("api/v1/lancamento")
public class LancamentoController {
	
	private LancamentoService service;
	
	@Autowired
	public LancamentoController(LancamentoService service) {
		this.service = service;
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<LancamentoDTO> buscarPorId(@PathVariable Long id){
		
		try {
			
			var lancamento = this.service.buscarPorId(id);
			
			return ResponseEntity.ok(lancamento);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@GetMapping()
	public ResponseEntity<List<LancamentoDTO>> listar(@RequestParam(name = "folha") Long idFolha){
		
		try {
			
			var lancamento = this.service.listar(idFolha);
			
			return ResponseEntity.ok(lancamento);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	
}
