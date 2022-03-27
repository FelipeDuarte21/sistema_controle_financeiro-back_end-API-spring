package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;

public class UsuarioSalvarDTO {

	@ApiModelProperty(value = "Nome do usuário")
	@NotNull(message = "Informe o campo nome")
	@Length(min=3, max=50, message = "O campo nome deve ter entre {min} a {max} caracteres")
	private String nome;
	
	@ApiModelProperty(value = "Email do usuário")
	@NotNull(message = "Informe o campo email")
	@Length(max=80, message = "O campo email deve ter até {max} caracteres")
	@Email(message = "o valor informado está fora do padrão para email")
	private String email;
	
	@ApiModelProperty(value = "Senha do usuário - deve ter entre 8 a 15 caracteres")
	@NotNull(message = "Informe o campo senha")
	@Length(min=8, max=15, message = "O campo senha deve ter entre {min} a {max} caracteres")
	private String senha;
	
	public UsuarioSalvarDTO() {
		
	}

	public UsuarioSalvarDTO(String nome,String email, String senha) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}
