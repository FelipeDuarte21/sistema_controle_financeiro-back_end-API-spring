package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Anotacao;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Categoria;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.AnotacaoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.AnotacaoDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository.AnotacaoRepository;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;


@Service
public class AnotacaoService {

	private AnotacaoRepository repository;
	
	private CategoriaService categoriaService;
	
	/*
	private RestricaoService restricaoService;*/
	
	@Autowired
	public AnotacaoService(AnotacaoRepository anotacaoCategoriaRepository, 
			CategoriaService categoriaService/*,RestricaoService restricaoService*/) {
		this.repository = anotacaoCategoriaRepository;
		this.categoriaService = categoriaService;
		//this.restricaoService = restricaoService;
	}
	
	public AnotacaoDTO salvar(Long idCategoria, AnotacaoDadosDTO anotacaoDadosDTO) {
		
		Categoria categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
		
		/*Verificar se a categoria pertence a conta do usuario*/
		
		Anotacao anotacao = new Anotacao(anotacaoDadosDTO);
		anotacao.setCategoria(categoria);
		
		anotacao = this.repository.save(anotacao);
		
		return new AnotacaoDTO(anotacao);
		
	}
	
	
	public AnotacaoDTO atualizar(Long id,AnotacaoDadosDTO anotacaoDTO) {
		
		Optional<Anotacao> optAnotacao = this.repository.findById(id);
		
		if(!optAnotacao.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! anotação não encontrada para o id informado!");
		
		/*Verificar se a anotação pertence a alguma categoria da conta do usuario*/
		
		Anotacao anotacao = optAnotacao.get();
		anotacao.setTitulo(anotacaoDTO.getTitulo());
		anotacao.setData(anotacaoDTO.getData());
		anotacao.setDescricao(anotacaoDTO.getDescricao());
		
		anotacao = this.repository.save(anotacao);
		
		return new AnotacaoDTO(anotacao);
		
	}

	public void excluir(Long id) {
		
		Optional<Anotacao> optAnotacao = this.repository.findById(id);
		
		if(!optAnotacao.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! anotação não encontrada para o id informado!");
		/*
		//Verifica a permissão
		this.restricaoService.verificarPermissaoConteudo(optAnotacao.get().getCategoria());*/
		
		this.repository.delete(optAnotacao.get());
		
	}

	public AnotacaoDTO buscarPorId(Long id) {
		
		var optAnotacao = this.repository.findById(id);
		
		if(!optAnotacao.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! anotação não encontrada para o id informado!");
		/*
		//Verifica a permissão
		this.restricaoService.verificarPermissaoConteudo(optAnotacao.get().getCategoria());*/
		
		return new AnotacaoDTO(optAnotacao.get());
		
	}
	
	public List<AnotacaoDTO> listar(Long idCategoria) {
		
		Categoria categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
	
		List<Anotacao> anotacoes = this.repository.findByCategoriaOrderByDataDesc(categoria);
		
		return anotacoes.stream().map(AnotacaoDTO::new).collect(Collectors.toList());
		
	}
	
	public AnotacaoDTO riscar(Long id) {
		
		Optional<Anotacao> optAnotacao = this.repository.findById(id);
		
		if(!optAnotacao.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! anotação não encontrada para o id informado!");
		
		/*
		//Verifica a permissão
		this.restricaoService.verificarPermissaoConteudo(optAnotacao.get().getCategoria());*/
		
		Anotacao anotacao = optAnotacao.get();
		
		anotacao.setRiscado(!anotacao.getRiscado());
		
		anotacao = this.repository.save(anotacao);
		
		return new AnotacaoDTO(anotacao);
		
	}

}
