package br.com.felipeduarte.APIControleFinanceiro.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAutenticacaoFiltro extends BasicAuthenticationFilter{
	
	private JWTUtil jwtUtil;

	public JWTAutenticacaoFiltro(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
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
		
		String usuario = this.jwtUtil.validarToken(token);
		
		if(usuario == null) return null;
		
		return new UsernamePasswordAuthenticationToken(usuario,null,new ArrayList<>());
 		
	}
	
	
}
