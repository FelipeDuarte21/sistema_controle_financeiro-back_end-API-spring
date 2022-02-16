package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.repository.CategoriaRepository;
import br.com.felipeduarte.APIControleFinanceiro.repository.UsuarioRepository;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.AuthorizationException;

@Service
public class RestricaoService {
	
	private CategoriaRepository categoriaRepository;
	
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	public RestricaoService(UsuarioRepository usuarioRepository,CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
		this.usuarioRepository = usuarioRepository;
	}
	
	private Optional<Usuario> getUsuarioLogado() {
		try {
			var email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			var usuario = this.usuarioRepository.findByEmail(email).get();
			return Optional.of(usuario);
			
		}catch(Exception e) {
			return Optional.empty();
		}
	}
	
	public Usuario getUsuario() {
		
		var optUsuario = this.getUsuarioLogado();
		
		if(!optUsuario.isPresent()) throw new AuthorizationException("Erro! Usuário não está logado!");
		
		return optUsuario.get();
		
	}
	
	public void verificarPermissaoConteudo(Categoria categoria) {
		
		var usuario = this.getUsuario();
		
		var categorias = this.categoriaRepository.findByUsuario(usuario);
		
		if(categorias.isEmpty()) throw new AuthorizationException("Acesso Indevido!");
			
		var categoriaEncontrada = 
				categorias.stream().filter(cat -> cat.getId().equals(categoria.getId()))
					.collect(Collectors.toList());
		
		if(categoriaEncontrada.isEmpty()) throw new AuthorizationException("Acesso Indevido!");
		
	}
	
	public void verificarUsuario(Long idComparado) {
		
		var usuario = this.getUsuario();
		
		if(!usuario.getId().equals(idComparado)) throw new AuthorizationException("Acesso Indevido!");
		
	}

}
