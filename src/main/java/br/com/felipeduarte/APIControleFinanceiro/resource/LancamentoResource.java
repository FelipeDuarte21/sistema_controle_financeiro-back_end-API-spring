package br.com.felipeduarte.APIControleFinanceiro.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectNotContentException;
import br.com.felipeduarte.APIControleFinanceiro.service.LancamentoService;

@RestController
@RequestMapping("/lancamento")
public class LancamentoResource {
	
	@Autowired
	private LancamentoService service;
	
	@PostMapping
	public ResponseEntity<Lancamento> salvar(@RequestBody @Valid LancamentoDTO lancamento){
		
		Lancamento lan = this.service.salvar(lancamento);
		
		if(lan == null) {
			throw new ObjectBadRequestException("Erro! Verifique as informações fornecidas!");
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lan);
	}
	
	@PutMapping
	public ResponseEntity<Lancamento> atualizar(@RequestBody @Valid LancamentoDTO lancamento){
		
		Lancamento lan = this.service.alterar(lancamento);
		
		if(lan == null) {
			throw new ObjectBadRequestException("Erro! Verifique as informações fornecidas!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(lan);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		boolean resp = this.service.excluir(id);
		
		if(resp == false) {
			throw new ObjectBadRequestException("Erro! Lançamento não encontrado!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Lancamento> buscarPorId(@PathVariable Long id){
		
		Lancamento lancamento = this.service.buscarPorId(id);
		
		if(lancamento == null) {
			throw new ObjectNotContentException("Erro! Lançamento não encontrado!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(lancamento);
		
	}
	
}
