package br.com.felipeduarte.APIControleFinanceiro.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;

@Component
public class JWTUtil {
	
	@Value("${jwt.signature}")
	private String assinatura;
	
	@Value("${jwt.expirationTime}")
	private Long tempoExpiracao;
	
	public String geradorToken(UsuarioDetalhe usuario) {
		
		String token = JWT.create()
				.withSubject(usuario.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + this.tempoExpiracao))
				.sign(Algorithm.HMAC512(this.assinatura));
		
		
		return token;
	}
	
	public String validarToken(String token) {
		
		try {
			String usuario = JWT.require(Algorithm.HMAC512(this.assinatura))
					.build().verify(token).getSubject();
			
			return usuario;
			
		}catch(TokenExpiredException e) {
			return null;
		}
		
	}
	
}
