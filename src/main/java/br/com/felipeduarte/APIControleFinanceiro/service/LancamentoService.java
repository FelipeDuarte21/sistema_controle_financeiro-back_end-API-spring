package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.TipoLancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.LancamentoRepository;

@Service
public class LancamentoService {
	
	@Autowired
	private LancamentoRepository repository;
	
	@Autowired
	private BalancoService balancoService;
	
	@Autowired
	private TipoLancamentoService tipoLancamentoService;
	
	
	public Lancamento salvar(LancamentoDTO lancamento) {
		
		Balanco balanco = this.balancoService.buscarPorId(lancamento.getBalanco());
		if(balanco == null) {
			return null;
		}
		
		TipoLancamento tipoLancamento = this.tipoLancamentoService.buscarPorValor(lancamento.getTipo());
		if(tipoLancamento == null) {
			return null;
		}
		
		Lancamento lan = Lancamento.converteParaLancamento(lancamento);
		
		lan.setTipo(tipoLancamento);
		lan.setBalanco(balanco);
		
		lan = this.repository.save(lan);
		
		this.balancoService.atualizarSaldo(lan.getBalanco());
		
		return lan;
	}
	
	public Lancamento alterar(LancamentoDTO lancamento) {
		
		if(lancamento.getId() == null || lancamento.getId() == 0) {
			return null;
		}
		
		Lancamento lan = this.buscarPorId(lancamento.getId());
		if(lan == null) {
			return null;
		}
		
		lan = this.salvar(lancamento);
		
		return lan;
	}
	
	public boolean excluir(Long id) {
		
		Lancamento lancamento = this.buscarPorId(id);
		
		if(lancamento == null) {
			return false;
		}
		
		Balanco balanco = lancamento.getBalanco();
		
		this.repository.delete(lancamento);
		
		this.balancoService.atualizarSaldo(balanco);
		
		return true;
	}
	
	public Lancamento buscarPorId(Long id) {
		
		Optional<Lancamento> lancamento = this.repository.findById(id);
		
		if(lancamento.isEmpty()) {
			return null;
		}
		
		return lancamento.get();
	}
	
}
