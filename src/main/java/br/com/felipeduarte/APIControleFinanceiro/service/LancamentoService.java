package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.LancamentoRepository;

@Service
public class LancamentoService {
	
	@Autowired
	private LancamentoRepository repository;
	
	public Lancamento salvar(LancamentoDTO lancamento) {
		return null;
	}
	
	public Lancamento alterar(LancamentoDTO lancamento) {
		return null;
	}
	
	public boolean excluir(Long id) {
		return false;
	}
	
	public Lancamento buscarPorId(Long id) {
		
		Optional<Lancamento> lancamento = this.repository.findById(id);
		
		if(lancamento.isEmpty()) {
			return null;
		}
		
		return lancamento.get();
	}
	
}
