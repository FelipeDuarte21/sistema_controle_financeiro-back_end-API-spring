package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.TipoLancamento;
import br.com.felipeduarte.APIControleFinanceiro.repository.TipoLancamentoRepository;

@Service
public class TipoLancamentoService {
	
	@Autowired
	private TipoLancamentoRepository repository;
	
	public List<TipoLancamento> listar(){
		return null;
	}
	
}
