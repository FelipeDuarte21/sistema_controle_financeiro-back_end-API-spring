package br.com.felipeduarte.APIControleFinanceiro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;

	public Usuario salvar(Usuario usuario) {
		return null;
	}
	
	public Usuario atualizar(Usuario usuario) {
		return null;
	}
	
	public boolean excluir(Long id) {
		return false;
	}
	
	public Usuario buscarPorId(Long id) {
		return null;
	}
	
	public Page<Usuario> listar(Integer page, Integer size, Integer order){
		
		Direction d = Direction.ASC;
		
		if(order == 1) {
			d = Direction.ASC;
		}else if(order == 2) {
			d = Direction.DESC;
		}
		
		PageRequest pageable = PageRequest.of(page, size, d, "nome");
		
		Page<Usuario> usuarios = this.repository.findAll(pageable);
		
		return usuarios;
		
	}
	
}
