package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginDTO {

	@NotNull(message = "Informe o campo email")
	@Length(max=80, message = "O campo email deve ter até {max} caracteres")
	@Email(message = "o valor informado está fora do padrão para email")
	private String email;
	
	@NotNull(message = "Informe o campo senha")
	@Length(min=8, max=15, message = "O campo senha deve ter entre {min} a {max} caracteres")
	private String senha;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public UsernamePasswordAuthenticationToken converter() {
		return new UsernamePasswordAuthenticationToken(email,senha);
	}
	
}
