package br.com.felipeduarte.APIControleFinanceiro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.LancamentoSalvo;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoSalvoDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoSalvoSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.LancamentoSalvoRepository;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

@Service
public class LancamentoSalvoService {
	
	private LancamentoSalvoRepository repository;
	
	private RestricaoService restricaoService;
	
	private TipoLancamentoService tipoLancamentoService;
	
	private CategoriaService categoriaService;
	
	@Autowired
	public LancamentoSalvoService(LancamentoSalvoRepository repository, RestricaoService restricaoService,
			TipoLancamentoService tipoLancamentoService, CategoriaService categoriaService) {
		this.repository = repository;
		this.restricaoService = restricaoService;
		this.tipoLancamentoService = tipoLancamentoService;
		this.categoriaService = categoriaService;
	}
	
	public LancamentoSalvoDTO cadastrar(Long idCategoria, LancamentoSalvoSalvarDTO lancamentoDTO) {
		
		var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
		
		try {
			
			var tipoLancamento = this.tipoLancamentoService.buscarPorValor(lancamentoDTO.getTipo());
			
			var lancamentoSalvo = new LancamentoSalvo(lancamentoDTO);
			lancamentoSalvo.setCategoria(categoria);
			lancamentoSalvo.setTipo(tipoLancamento);
				
			lancamentoSalvo = this.repository.save(lancamentoSalvo);
				
			return new LancamentoSalvoDTO(lancamentoSalvo);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new IllegalParameterException(ex.getMessage());
			
		}
		
	}
	
	public LancamentoSalvoDTO atualizar(Long id, LancamentoSalvoSalvarDTO lancamentoDTO) {
		
		var optLancamentoSalvo = this.repository.findById(id);
		
		if(!optLancamentoSalvo.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! lancamento não encontrado para o id informado!");
		
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optLancamentoSalvo.get().getCategoria());
		
		var lancamento = optLancamentoSalvo.get();
		lancamento.setNome(lancamentoDTO.getNome());
		lancamento.setDescricao(lancamentoDTO.getDescricao());
		lancamento.setValor(lancamentoDTO.getValor());
		
		try {
			
			var tipoLancamento = this.tipoLancamentoService.buscarPorValor(lancamentoDTO.getTipo());
			
			lancamento.setTipo(tipoLancamento);
			
			lancamento = this.repository.save(lancamento);
			
			return new LancamentoSalvoDTO(lancamento);

		}catch(ObjectNotFoundFromParameterException ex) {
			throw new IllegalParameterException(ex.getMessage());
			
		}
		
	}
	
	public void excluir(Long id) {
		
		var optLancamentoSalvo = this.repository.findById(id);
		
		if(!optLancamentoSalvo.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! lancamento não encontrado para o id informado!");
		
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optLancamentoSalvo.get().getCategoria());
		
		this.repository.delete(optLancamentoSalvo.get());
		
	}
	
	public LancamentoSalvoDTO buscarPorId(Long id) {
		
		var optLancamentoSalvo = this.repository.findById(id);
		
		if(!optLancamentoSalvo.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! lancamento não encontrado para o id informado!");
		
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optLancamentoSalvo.get().getCategoria());
		
		return new LancamentoSalvoDTO(optLancamentoSalvo.get());
		
	}
	
	public Page<LancamentoSalvoDTO> listar(Long idCategoria, Pageable paginacao) {
		
		var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
			
		//verificaPermissao
		this.restricaoService.verificarPermissaoConteudo(categoria);
			
		var pagLancamentoSalvo = this.repository.findByCategoria(categoria, paginacao);
			
		return pagLancamentoSalvo.map(LancamentoSalvoDTO::new);
		
	}

}
