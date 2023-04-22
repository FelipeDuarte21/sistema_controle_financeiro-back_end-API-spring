package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.LancamentoFixo;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoFixoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoFixoDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.enums.TipoLancamento;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository.LancamentoFixoRepository;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.IllegalParameterException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;

@Service
public class LancamentoFixoService {
	
	private LancamentoFixoRepository repository;
	
	/*private RestricaoService restricaoService;*/
	
	private CategoriaService categoriaService;
	
	@Autowired
	public LancamentoFixoService(LancamentoFixoRepository repository/*, RestricaoService restricaoService*/,
			CategoriaService categoriaService) {
		this.repository = repository;
		//this.restricaoService = restricaoService;
		this.categoriaService = categoriaService;
	}
	
	public LancamentoFixoDTO cadastrar(Long idCategoria, LancamentoFixoDadosDTO lancamentoFixoDadosDTO) {
		
		try {
			
			var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
			
			var optLancamentoFixo = this.repository.findByDescricao(lancamentoFixoDadosDTO.getDescricao());
			
			if(optLancamentoFixo.isPresent()) return new LancamentoFixoDTO(optLancamentoFixo.get());
			
			var tipoLancamento = TipoLancamento.toEnum(lancamentoFixoDadosDTO.getTipo());
			
			var lancamentoFixo = new LancamentoFixo(lancamentoFixoDadosDTO);
			lancamentoFixo.setCategoria(categoria);
			lancamentoFixo.setTipo(tipoLancamento);
				
			lancamentoFixo = this.repository.save(lancamentoFixo);
				
			return new LancamentoFixoDTO(lancamentoFixo);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new IllegalParameterException(ex.getMessage());
			
		}
		
	}
	
	public LancamentoFixoDTO atualizar(Long id, LancamentoFixoDadosDTO lancamentoFixoDadosDTO) {
		
		var optLancamentoFixo = this.repository.findById(id);
		
		if(!optLancamentoFixo.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! lancamento não encontrado para o id informado!");
		/*
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optLancamentoSalvo.get().getCategoria());*/
		
		var lancamento = optLancamentoFixo.get();
		lancamento.setTitulo(lancamentoFixoDadosDTO.getTitulo());
		lancamento.setDescricao(lancamentoFixoDadosDTO.getDescricao());
		lancamento.setValor(lancamentoFixoDadosDTO.getValor());
		
		try {
			
			var tipoLancamento = TipoLancamento.toEnum(lancamentoFixoDadosDTO.getTipo());
			
			lancamento.setTipo(tipoLancamento);
			
			lancamento = this.repository.save(lancamento);
			
			return new LancamentoFixoDTO(lancamento);

		}catch(ObjectNotFoundFromParameterException ex) {
			throw new IllegalParameterException(ex.getMessage());
			
		}
		
	}
	
	public void excluir(Long id) {
		
		var optLancamentoFixo = this.repository.findById(id);
		
		if(!optLancamentoFixo.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! lancamento não encontrado para o id informado!");
		/*
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optLancamentoSalvo.get().getCategoria());*/
		
		this.repository.delete(optLancamentoFixo.get());
		
	}
	
	public LancamentoFixoDTO buscarPorId(Long id) {
		
		var optLancamentoFixo = this.repository.findById(id);
		
		if(!optLancamentoFixo.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! lancamento não encontrado para o id informado!");
		/*
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optLancamentoFixo.get().getCategoria());*/
		
		return new LancamentoFixoDTO(optLancamentoFixo.get());
		
	}
	
	public List<LancamentoFixoDTO> listar(Long idCategoria) {
		
		var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
		
		/*
		//verificaPermissao
		this.restricaoService.verificarPermissaoConteudo(categoria);*/
			
		var lancamentosFixos = this.repository.findByCategoria(categoria);
			
		return lancamentosFixos.stream().map(LancamentoFixoDTO::new).collect(Collectors.toList());
		
	}

}
