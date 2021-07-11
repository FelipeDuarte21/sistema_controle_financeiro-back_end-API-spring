package br.com.felipeduarte.APIControleFinanceiro.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.repository.UsuarioRepository;

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

}
