package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.AuthorizationException;

@Service
public class RestricaoService {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	private Usuario getUsuarioLogado() {
		try {
			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Usuario usuario = this.usuarioService.buscarPorEmail(email,false);
			return usuario;
			
		}catch(Exception e) {
			return null;
		}
	}
	
	public Usuario getUsuario() {
		
		Usuario usuario = this.getUsuarioLogado();
		
		if(usuario == null) throw new AuthorizationException("Usuário não logado");
		
		return usuario;
	}
	
	public void verificarPermissaoConteudo(Categoria categoria) {
		
		Usuario usuario = this.getUsuario();
		
		boolean resp = false;
		
		List<Categoria> categorias = this.categoriaService.buscarPorUsuario(usuario);
		
		if(categorias != null) {
			for(Categoria cat: categorias) {
				if(cat.getId().equals(categoria.getId())) {
					resp = true;
					break;
				}
			}
		}
		
		if(resp == false) {
			throw new AuthorizationException("Acesso Indevido!");
		}
	}
	
	public void verificarUsuario(Long idComparado) {
		
		Usuario usuario = this.getUsuario();
		
		if(!usuario.getId().equals(idComparado)) {
			throw new AuthorizationException("Acesso Indevido!");
		}
		
	}

}
