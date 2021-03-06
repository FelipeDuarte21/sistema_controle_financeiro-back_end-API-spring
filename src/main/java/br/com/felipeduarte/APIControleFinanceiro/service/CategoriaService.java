package br.com.felipeduarte.APIControleFinanceiro.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.CategoriaDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.CategoriaSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.CategoriaRepository;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

@Service
public class CategoriaService {
	
	private Clock clock;
	
	private CategoriaRepository repository;
	
	private RestricaoService restricaoService;
	
	@Autowired
	public CategoriaService(CategoriaRepository repository, Clock clock, RestricaoService restricaoService) {
		this.repository = repository;
		this.clock = clock;
		this.restricaoService = restricaoService;
	}

	@Transactional(rollbackOn = Exception.class)
	public CategoriaDTO salvar(CategoriaSalvarDTO categoriaDTO) {
		//Obtem o usuário logado
		Usuario usuario = this.restricaoService.getUsuario();
		
		var optCategoria = this.repository.findByNomeAndUsuario(categoriaDTO.getNome(),usuario);
		
		if(optCategoria.isPresent()) throw new IllegalParameterException("Erro! categoria já cadastrada!");
		
		var categoria = new Categoria(categoriaDTO);
		categoria.setUsuario(usuario);
		categoria.setDataCadastro(LocalDate.now(clock.getZone()));
		
		categoria = this.repository.save(categoria);
		
		return new CategoriaDTO(categoria);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public CategoriaDTO atualizar(Long id, CategoriaSalvarDTO categoriaDTO) {
		
		if(id == null) throw new IllegalParameterException("Erro! id não pode ser nullo");
		if(id == 0) throw new IllegalParameterException("Erro! id não pode ser 0");
		
		var optCategoria = this.repository.findById(id);
		
		if(!optCategoria.isPresent())
			throw new ObjectNotFoundFromParameterException(
					"Erro! categoria não econtrada para o id informado!");
		
		//Verifica se categoria pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(optCategoria.get());
		
		var categoria = optCategoria.get();
		categoria.setNome(categoriaDTO.getNome());
		categoria.setDescricao(categoriaDTO.getDescricao());
		
		categoria = this.repository.save(categoria);
		
		return new CategoriaDTO(categoria);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void excluir(Long id) {
		
		var optCategoria = this.repository.findById(id);
		
		if(!optCategoria.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! categoria não econtrada para o id informado!");
		
		//Verifica se categoria pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(optCategoria.get());
		
		this.repository.delete(optCategoria.get());
		
	}
	
	public CategoriaDTO buscarPorId(Long id) {
		
		var categoria = buscarPorIdInterno(id);
		
		return new CategoriaDTO(categoria);
		
	}
	
	public Categoria buscarPorIdInterno(Long id) {
		
		var optCategoria = this.repository.findById(id);
		
		if(!optCategoria.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! categoria não econtrada para o id informado!");
		
		//Verifica se categoria pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(optCategoria.get());
		
		return optCategoria.get();
	}
	
	public List<CategoriaDTO> buscarPorUsuario(Usuario usuario) {
		
		var categorias = this.repository.findByUsuario(usuario);
		
		if(categorias.isEmpty()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! Não existe categorias cadastradas para o usuario informado!");
		
		return categorias.stream().map(CategoriaDTO::new).collect(Collectors.toList());
		
	}
	
	public Page<CategoriaDTO> listar(Pageable paginacao){
		
		//Obtem o usuário logado
		var usuario = this.restricaoService.getUsuario();
		
		var pageCategorias = this.repository.findByUsuario(usuario, paginacao);
		
		return pageCategorias.map(CategoriaDTO::new);
		
	}
	
	
}
