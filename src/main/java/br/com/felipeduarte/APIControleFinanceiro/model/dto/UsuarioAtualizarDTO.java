package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class UsuarioAtualizarDTO {
	
	@NotNull(message = "Informe o campo id")
	private Long id;
	
	@NotNull(message = "Informe o campo nome")
	@Length(min=3, max=50, message = "O campo nome deve ter entre {min} a {max} caracteres")
	private String nome;
	
	@NotNull(message = "Informe o campo email")
	@Length(max=80, message = "O campo email deve ter até {max} caracteres")
	@Email(message = "o valor informado está fora do padrão para email")
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
