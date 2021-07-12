package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.UsuarioAtualizarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.UsuarioSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.enums.TipoUsuario;
import br.com.felipeduarte.APIControleFinanceiro.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private BCryptPasswordEncoder bCrypt;
	
	@Autowired
	private RestricaoService restricaoService;

	public Usuario salvar(UsuarioSalvarDTO usuario) {
		
		Optional<Usuario> u = this.repository.findByEmail(usuario.getEmail());
		
		if(u.isPresent()) {
			return null;
		}
		
		Usuario usu = Usuario.converteParaUsuario(usuario);
		
		usu.setSenha(this.bCrypt.encode(usu.getSenha()));
		
		usu.getTipo().add(TipoUsuario.USUARIO.getCodigo());
		
		usu = this.repository.save(usu);
		return usu;
		
	}
	
	public Usuario atualizar(UsuarioAtualizarDTO usuario) {
		
		if(usuario.getId() == null || usuario.getId() == 0L) {
			return null;
		}
		
		Optional<Usuario> u1 = this.repository.findById(usuario.getId());
		
		if(u1.isEmpty()) {
			return null;
		}
		
		//Verifica se usuario é o mesmo que está logado
		this.restricaoService.verificarUsuario(usuario.getId());
		
		//Verifica se o caso o email foi alterado não esteja já cadastrado!
		if(!usuario.getEmail().equals(u1.get().getEmail())) {
			
			Optional<Usuario> u2 = this.repository.findByEmail(usuario.getEmail());
			
			if(u2.isPresent()) {
				return null;
			}
			
		}
		
		Usuario usu = Usuario.converteParaUsuario(usuario);
		usu.setSenha(u1.get().getSenha());
		usu.getTipo().add(TipoUsuario.USUARIO.getCodigo());
		
		usu = this.repository.save(usu);
		
		return usu;
		
	}
	
	public boolean excluir(Long id) {
		
		Optional<Usuario> usu = this.repository.findById(id);
		
		if(usu.isEmpty()) {
			return false;
		}
		
		//Verifica se usuario é o mesmo que está logado
		this.restricaoService.verificarUsuario(id);
		
		this.repository.delete(usu.get());
		return true;
	}
	
	public Usuario buscarPorId(Long id) {
		
		Optional<Usuario> usuario =  this.repository.findById(id);
		
		if(usuario.isEmpty()) {
			return null;
		}
		
		return usuario.get();
		
	}
	
	public Usuario buscarPorEmail(String email) {
		
		Optional<Usuario> usuario = this.repository.findByEmail(email);
		
		if(usuario.isEmpty()) {
			return null;
		}
		
		return usuario.get();
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
