package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LoginDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.TokenDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.security.JWTUtil;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.security.UsuarioDetalhe;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.IllegalParameterException;

@Service
public class AuthService {
	
	private JWTUtil jwtUtil;
	private AuthenticationManager authManager;
	
	@Autowired
	public AuthService(JWTUtil jwtUtil,AuthenticationManager authManager) {
		this.jwtUtil = jwtUtil;
		this.authManager = authManager;
	}
	
	public TokenDTO login(LoginDTO login) {
		
		UsernamePasswordAuthenticationToken dadosLogin = login.converter();
		
		try {
			
			Authentication authentication = authManager.authenticate(dadosLogin);
			
			var usuario = (UsuarioDetalhe) authentication.getPrincipal();
			
			String token = this.jwtUtil.geradorToken(usuario);
			
			return new TokenDTO(token, "Bearer",usuario.getId());
			
		}catch(AuthenticationException ex) {
			throw new IllegalParameterException("Erro! email e/ou senha incorretos!");
			
		}
		
	}
	
}
