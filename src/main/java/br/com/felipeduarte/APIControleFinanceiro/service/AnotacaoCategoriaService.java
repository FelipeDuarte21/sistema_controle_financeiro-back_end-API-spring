package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.AnotacaoCategoria;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.AnotacaoCategoriaDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.AnotacaoCategoriaSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.AnotacaoCategoriaRepository;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

@Service
public class AnotacaoCategoriaService {

	private AnotacaoCategoriaRepository repository;
	
	private CategoriaService categoriaService;
	
	private RestricaoService restricaoService;
	
	@Autowired
	public AnotacaoCategoriaService(AnotacaoCategoriaRepository anotacaoCategoriaRepository, 
			CategoriaService categoriaService, RestricaoService restricaoService) {
		this.repository = anotacaoCategoriaRepository;
		this.categoriaService = categoriaService;
		this.restricaoService = restricaoService;
	}
	
	public AnotacaoCategoriaDTO salvar(Long idCategoria, AnotacaoCategoriaSalvarDTO anotacaoDTO) {
		
		var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
		
		var anotacao = new AnotacaoCategoria(anotacaoDTO);
		anotacao.setCategoria(categoria);
		
		anotacao = this.repository.save(anotacao);
		
		return new AnotacaoCategoriaDTO(anotacao);
		
	}
	
	
	public AnotacaoCategoriaDTO atualizar(Long idCategoria, Long id, 
			AnotacaoCategoriaSalvarDTO anotacaoDTO) {
		
		var optAnotacao = this.repository.findById(id);
		
		if(!optAnotacao.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! anotação não encontrada para o id informado!");
		
		//Verifica a permissão
		this.restricaoService.verificarPermissaoConteudo(optAnotacao.get().getCategoria());
		
		var anotacao = optAnotacao.get();
		anotacao.setTitulo(anotacaoDTO.getTitulo());
		anotacao.setData(anotacaoDTO.getData());
		anotacao.setDescricao(anotacaoDTO.getDescricao());
		
		anotacao = this.repository.save(anotacao);
		
		return new AnotacaoCategoriaDTO(anotacao);
		
	}
	
	public AnotacaoCategoriaDTO riscar(Long id) {
		
		var optAnotacao = this.repository.findById(id);
		
		if(!optAnotacao.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! anotação não encontrada para o id informado!");
		
		//Verifica a permissão
		this.restricaoService.verificarPermissaoConteudo(optAnotacao.get().getCategoria());
		
		var anotacao = optAnotacao.get();
		
		anotacao.setRiscado(!anotacao.getRiscado());
		
		anotacao = this.repository.save(anotacao);
		
		return new AnotacaoCategoriaDTO(anotacao);
		
	}

	public void excluir(Long id) {
		
		var optAnotacao = this.repository.findById(id);
		
		if(!optAnotacao.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! anotação não encontrada para o id informado!");
		
		//Verifica a permissão
		this.restricaoService.verificarPermissaoConteudo(optAnotacao.get().getCategoria());
		
		this.repository.delete(optAnotacao.get());
		
	}

	public AnotacaoCategoriaDTO buscarPorId(Long id) {
		
		var optAnotacao = this.repository.findById(id);
		
		if(!optAnotacao.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! anotação não encontrada para o id informado!");
		
		//Verifica a permissão
		this.restricaoService.verificarPermissaoConteudo(optAnotacao.get().getCategoria());
		
		return new AnotacaoCategoriaDTO(optAnotacao.get());
		
	}
	
	public Page<AnotacaoCategoriaDTO> listar(Long idCategoria, int page, int size, int order) {
		
		var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
		
		if(page < 0) 
			throw new IllegalParameterException("Erro! o número da página não pode ser negativo!");
		
		if(size < 1) 
			throw new IllegalParameterException("Erro! a quantidade de elementos na página é no mínimo 1");
		
		var direction = Direction.ASC;
		
		if(order == 2) direction = Direction.DESC;
		
		var pageable = PageRequest.of(page, size,direction,"data");
		
		var pagAnotacao = this.repository.findByCategoria(categoria, pageable);
		
		var pageAnotacaoDTO = new PageImpl<AnotacaoCategoriaDTO>(
				pagAnotacao.getContent().stream().map(AnotacaoCategoriaDTO::new)
					.collect(Collectors.toList()),pagAnotacao.getPageable(),
						pagAnotacao.getTotalElements());
		
		return pageAnotacaoDTO;
		
	}

}
