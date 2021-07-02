package br.com.felipeduarte.APIControleFinanceiro.service;

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
	
	@Autowired
	private CategoriaRepository repository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public Categoria salvar(CategoriaDTO categoria) {
		
		Categoria cat = this.repository.findByNome(categoria.getNome());
		
		if(cat != null) {
			return null;
		}
		
		cat = Categoria.converteParaCategoria(categoria);
		
		//Deverá trocar para o usuário logado
		Usuario usuario = this.usuarioService.buscarPorId(1L);
		cat.setUsuario(usuario);
		
		cat = this.repository.save(cat);
		
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
		
		Categoria c = Categoria.converteParaCategoria(categoria);
		c.setUsuario(cat.get().getUsuario());
		
		c = this.repository.save(c);
		return c;
	}
	
	public boolean excluir(Integer id) {
		return false;
	}
	
	public Page<Categoria> listar(Integer page, Integer size, Integer order){
		Direction d = Direction.ASC;
		
		if(order == 1) {
			d = Direction.ASC;
		}else if(order == 2) {
			d = Direction.DESC;
		}
		
		PageRequest pageable = PageRequest.of(page, size,d,"nome");
		
		//Depois buscar o usuário que estiver autenticado, por enquanto é só pra desenvolvimento
		Usuario usuario = this.usuarioService.buscarPorId(1L);
		
		Page<Categoria> categorias = this.repository.findByUsuario(usuario, pageable);
		
		return categorias;
	}
	
}
