package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JWTUtil {
	
	@Value("${jwt.signature}")
	private String assinatura;
	
	@Value("${jwt.expirationTime}")
	private Long tempoExpiracao;
	
	public String geradorToken(UsuarioDetalhe usuario) {
		
		String token = JWT.create()
				.withSubject(usuario.getId().toString())
				.withExpiresAt(new Date(System.currentTimeMillis() + this.tempoExpiracao))
				.sign(Algorithm.HMAC512(this.assinatura));
		
		
		return token;
	}
	
	public boolean validarToken(String token) {
		
		try {
			JWT.require(Algorithm.HMAC512(this.assinatura)).build().verify(token);
			
			return true;
			
		}catch(Exception ex) {
			return false;
			
		}
		
	}
	
	public Long getIdUsuario(String token) {
		
		try {
			String id = JWT.require(Algorithm.HMAC512(this.assinatura))
					.build().verify(token).getSubject();
			
			return Long.parseLong(id);
			
		}catch(Exception ex) {
			return null;
			
		}
		
	}
	
}
