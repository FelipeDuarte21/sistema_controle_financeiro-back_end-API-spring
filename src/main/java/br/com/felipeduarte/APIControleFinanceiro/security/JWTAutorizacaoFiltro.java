package br.com.felipeduarte.APIControleFinanceiro.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAutorizacaoFiltro extends BasicAuthenticationFilter{
	
	private JWTUtil jwtUtil;
	
	private UsuarioDetalheService usuarioDetalheService;
	

	public JWTAutorizacaoFiltro(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
			UsuarioDetalheService usuarioDetalheService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.usuarioDetalheService = usuarioDetalheService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String header = request.getHeader("Authorization");
		
		if(header != null && header.startsWith("Bearer ")) {
			UsernamePasswordAuthenticationToken auth = this.getAuthentication(header.substring(7));
			
			if(auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			
		}
		
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		
		String usuarioEmail = this.jwtUtil.validarToken(token);
		
		if(usuarioEmail == null) return null;
		
		try {
			UsuarioDetalhe usuario = (UsuarioDetalhe) this.usuarioDetalheService.loadUserByUsername(usuarioEmail);
			return new UsernamePasswordAuthenticationToken(usuarioEmail,null,usuario.getAuthorities());
		
		}catch(UsernameNotFoundException e) {
			return null;
		}
		
	}
	
	
}
