package br.com.felipeduarte.APIControleFinanceiro.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.CategoriaDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	private static final String TIME_ZONE = "America/Sao_Paulo";
	
	@Autowired
	private CategoriaRepository repository;
	
	@Autowired
	private BalancoService balancoService;
	
	@Autowired
	private RestricaoService restricaoService;
	
	public Categoria salvar(CategoriaDTO categoria) {
		//Obtem o usuário logado
		Usuario usuario = this.restricaoService.getUsuario();
		
		Categoria cat = this.repository.findByNomeAndUsuario(categoria.getNome(),usuario);
		
		if(cat != null) {
			return null;
		}
		
		cat = Categoria.converteParaCategoria(categoria);
		cat.setUsuario(usuario);
		
		//Tratando a data
		cat.setDataCadastro(LocalDate.now(ZoneId.of(TIME_ZONE)));
		
		cat = this.repository.save(cat);
		
		this.balancoService.cadastrar(cat);
		
		return cat;
	}
	
	public Categoria atualizar(CategoriaDTO categoria) {
		
		if(categoria.getId() == null) {
			return null;
		}
		
		Optional<Categoria> cat = this.repository.findById(categoria.getId());
		
		if(cat.isEmpty()) {
			return null;
		}
		
		//Verifica se categoria pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(cat.get());
		
		Categoria c = Categoria.converteParaCategoria(categoria);
		c.setUsuario(cat.get().getUsuario());
		
		c = this.repository.save(c);
		return c;
	}
	
	public boolean excluir(Long id) {
		
		Optional<Categoria> cat = this.repository.findById(id);
		
		if(cat.isEmpty()) {
			return false;
		}
		
		//Verifica se categoria pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(cat.get());
		
		this.repository.delete(cat.get());
		return true;
	}
	
	public Categoria buscarPorId(Long id) {
		
		Optional<Categoria> categoria = this.repository.findById(id);
		
		if(categoria.isEmpty()) {
			return null;
		}
		
		//Verifica se categoria pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(categoria.get());
		
		return categoria.get();
	}
	
	public List<Categoria> buscarPorUsuario(Usuario usuario) {
		
		List<Categoria> categorias = this.repository.findByUsuario(usuario);
		
		if(categorias.isEmpty()) return null;
		
		return categorias;
	}
	
	public Page<Categoria> listar(Integer page, Integer size, Integer order){
		Direction d = Direction.ASC;
		
		if(order == 1) {
			d = Direction.ASC;
		}else if(order == 2) {
			d = Direction.DESC;
		}
		
		PageRequest pageable = PageRequest.of(page, size,d,"nome");
		
		//Obtem o usuário logado
		Usuario usuario = this.restricaoService.getUsuario();
		
		Page<Categoria> categorias = this.repository.findByUsuario(usuario, pageable);
		
		return categorias;
	}
	
	public void abrirBalanco(){
		
		List<Categoria> categorias = this.repository.findAll();
		
		if(categorias != null) {
			
			categorias.forEach(categoria -> {
				this.balancoService.cadastrar(categoria);
			});
			
		}
		
	}
	
}
