package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.TipoLancamento;
import br.com.felipeduarte.APIControleFinanceiro.repository.TipoLancamentoRepository;

@Service
public class TipoLancamentoService {
	
	@Autowired
	private TipoLancamentoRepository repository;
	
	public List<TipoLancamento> listar(){
		
		List<TipoLancamento> tipos = this.repository.findAll();
		
		if(tipos.isEmpty() || tipos == null) {
			return null;
		}
		
		return tipos;
	}
	
	public TipoLancamento buscarPorValor(Integer valor) {
		
		Optional<TipoLancamento> tl = this.repository.findByValor(valor);
		
		if(tl.isEmpty()) {
			return null;
		}
		
		return tl.get();
	}
	
}
