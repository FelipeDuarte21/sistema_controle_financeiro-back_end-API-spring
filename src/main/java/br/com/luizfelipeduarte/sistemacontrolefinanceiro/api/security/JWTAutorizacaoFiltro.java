package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTAutorizacaoFiltro extends OncePerRequestFilter{
	
	private JWTUtil jwtUtil;
	
	private UsuarioDetalheService usuarioDetalheService;
	

	public JWTAutorizacaoFiltro(JWTUtil jwtUtil, UsuarioDetalheService usuarioDetalheService) {
		this.jwtUtil = jwtUtil;
		this.usuarioDetalheService = usuarioDetalheService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recuperaToken(request);
		
		boolean valido = jwtUtil.validarToken(token);
		
		if(valido) {
			autenticarUsuario(token);
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	private String recuperaToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		
		if(token == null || token.isEmpty() || !token.startsWith("Bearer")) {
			return null;
		}
		
		return token.substring(7,token.length());
	}

	private void autenticarUsuario(String token) {
		Long idUsuario = jwtUtil.getIdUsuario(token);
		UsuarioDetalhe usuario = (UsuarioDetalhe) usuarioDetalheService.loadUserById(idUsuario);
		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(usuario,null, usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
}
