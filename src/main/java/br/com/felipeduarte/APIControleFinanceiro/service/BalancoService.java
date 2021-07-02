package br.com.felipeduarte.APIControleFinanceiro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.repository.BalancoRepository;

@Service
public class BalancoService {
	
	@Autowired
	private BalancoRepository repository;
	
	public Balanco recuperarAtual(Long idCategoria) {
		return null;
	}
	
	public Balanco recuperarPorData(Integer mes, Integer ano, Long idCategoria) {
		return null;
	}
	
}
