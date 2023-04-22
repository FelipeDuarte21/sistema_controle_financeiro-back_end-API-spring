package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Folha;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Lancamento;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.enums.TipoLancamento;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository.LancamentoRepository;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.IllegalParameterException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;

@Service
public class LancamentoService {
	
	private Clock clock;
	
	private LancamentoRepository repository;
	
	@Autowired
	public LancamentoService(Clock clock, LancamentoRepository repository) {
		this.clock = clock;
		this.repository = repository;
	}

	@Transactional(rollbackOn = Exception.class)
	public LancamentoDTO salvar(Folha folha, LancamentoDadosDTO lancamentoDadosDTO) {

		var lancamento = new Lancamento(lancamentoDadosDTO);
		lancamento.setTipo(TipoLancamento.toEnum(lancamentoDadosDTO.getTipo()));
		lancamento.setFolha(folha);
		lancamento.setDataRegistro(LocalDateTime.now(clock));
		
		lancamento = this.repository.save(lancamento);
		
		return new LancamentoDTO(lancamento);
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public LancamentoDTO alterar(Long id, LancamentoDadosDTO lancamentoDadosDTO) {
		
		if(id == null) throw new IllegalParameterException("Erro! id não pode ser nullo");
		if(id == 0) throw new IllegalParameterException("Erro! id não pode ser 0");
		
		var optLancamento = this.repository.findById(id);
		
		if(!optLancamento.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! lançamento não encontrado para o id informado!");
		
		var lancamento = optLancamento.get();
		lancamento.setId(id);
		lancamento.setTitulo(lancamentoDadosDTO.getTitulo());
		lancamento.setDescricao(lancamentoDadosDTO.getDescricao());
		lancamento.setValor(lancamentoDadosDTO.getValor());
		lancamento.setDataLancamento(lancamentoDadosDTO.getData());
		lancamento.setTipo(TipoLancamento.toEnum(lancamentoDadosDTO.getTipo()));
		
		lancamento = this.repository.save(lancamento);
		
		return new LancamentoDTO(lancamento);
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void excluir(Long id) {
		
		var optLancamento = this.repository.findById(id);
		
		if(!optLancamento.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro!  lançamento não encontrado para o id informado!");
	
		
		this.repository.delete(optLancamento.get());
		
	}
	
	public LancamentoDTO buscarPorId(Long id) {
		
		var lancamento = buscarPorIdInterno(id); 
		
		return new LancamentoDTO(lancamento);
		
	}
	
	public Lancamento buscarPorIdInterno(Long id) {
		
		var optLancamento = this.repository.findById(id);
		
		if(!optLancamento.isPresent()) throw new 
			ObjectNotFoundFromParameterException("Erro! lançamento não encontrado para o id informado!");
		
		
		return optLancamento.get();
		
	}
	
	public List<LancamentoDTO> listar(Folha folha) {
			
		var lancamentos = this.repository.findByFolhaOrderByDataRegistroDesc(folha);
			
		return lancamentos.stream().map(LancamentoDTO::new).collect(Collectors.toList());
		
	}
	
	public List<LancamentoDTO> listar(Long idFolha) {
		
		var lancamentos = this.repository.findByFolha_IdOrderByDataRegistroDesc(idFolha);
			
		return lancamentos.stream().map(LancamentoDTO::new).collect(Collectors.toList());
		
	}
	
}
