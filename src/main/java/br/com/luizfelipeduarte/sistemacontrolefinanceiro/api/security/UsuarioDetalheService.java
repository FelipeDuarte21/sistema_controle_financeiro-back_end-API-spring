package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Usuario;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository.UsuarioRepository;

@Service
public class UsuarioDetalheService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Optional<Usuario> usuario = this.usuarioRepository.findByEmail(email);
		
		if(usuario.isEmpty()) throw new UsernameNotFoundException(email);
		
		return new UsuarioDetalhe(usuario.get().getId(),usuario.get().getEmail(),
				usuario.get().getSenha(),usuario.get().getTipo());
		
	}
	
	public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
		
		Optional<Usuario> usuario = this.usuarioRepository.findById(id);
		
		if(usuario.isEmpty()) throw new UsernameNotFoundException(id.toString());
		
		return new UsuarioDetalhe(usuario.get().getId(),usuario.get().getEmail(),
				usuario.get().getSenha(),usuario.get().getTipo());
		
	}
	
	public Optional<UsuarioDetalhe> getUsuarioAutenticado() throws Exception {
		try {
			var usuarioDetalhe = (UsuarioDetalhe) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			var usuario = this.usuarioRepository.findByEmail(usuarioDetalhe.getUsername()).get();
			return Optional.of(new UsuarioDetalhe(usuario));
			
		}catch(Exception e) {
			throw new Exception("Erro! O usuario nao está autenticado!");
		}
	}

}
