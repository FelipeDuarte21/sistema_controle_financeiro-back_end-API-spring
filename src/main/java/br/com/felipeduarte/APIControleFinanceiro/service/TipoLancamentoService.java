package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.TipoLancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.TipoLancamentoDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.TipoLancamentoRepository;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

@Service
public class TipoLancamentoService {
	
	private TipoLancamentoRepository repository;
	
	@Autowired
	public TipoLancamentoService(TipoLancamentoRepository repository) {
		this.repository = repository;
	}
	
	public List<TipoLancamentoDTO> listar(){
		
		var tiposLancamentos = this.repository.findAll();
		
		return tiposLancamentos.stream().map(TipoLancamentoDTO::new).collect(Collectors.toList());
		
	}
	
	public TipoLancamento buscarPorValor(Integer valor) {
		
		var optTipoLancamento = this.repository.findByValor(valor);
		
		if(!optTipoLancamento.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! tipo de lançamento não encontrado para o id informado!");
		
		return optTipoLancamento.get();
		
	}
	
}
