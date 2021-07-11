package br.com.felipeduarte.APIControleFinanceiro.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthorizacaoFiltro extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	
	public JWTAuthorizacaoFiltro(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		try {
			UsuarioDTO usuario = new ObjectMapper().readValue(request.getInputStream(),UsuarioDTO.class);
		
			return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					usuario.getEmail(),
					usuario.getSenha(),
					new ArrayList<>()
			));
		
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		UsuarioDetalhe usuario = (UsuarioDetalhe) authResult.getPrincipal();
		
		String token = this.jwtUtil.geradorToken(usuario);
		
		response.addHeader("Authorization", "Bearer " + token);
		
	}
	
}
