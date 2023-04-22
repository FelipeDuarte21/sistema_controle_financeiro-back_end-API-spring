package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class UsuarioDadosDTO {

	@NotNull(message = "Informe o campo nome")
	@Length(min=3, max=50, message = "O campo nome deve ter entre {min} a {max} caracteres")
	private String nome;
	
	@NotNull(message = "Informe o campo email")
	@Length(max=80, message = "O campo email deve ter até {max} caracteres")
	@Email(message = "o valor informado está fora do padrão para email")
	private String email;
	
	@NotNull(message = "Informe o campo senha")
	@Length(min=8, max=15, message = "O campo senha deve ter entre {min} a {max} caracteres")
	private String senha;
	
	@NotNull(message = "Informe a renda mensal total")
	private BigDecimal rendaMensalTotal;
	
	private List<CategoriaPersonalizadaDTO> categorias = new ArrayList<>(); 
	
	public UsuarioDadosDTO() {
		
	}

	public UsuarioDadosDTO(String nome,String email, String senha) {
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

	public BigDecimal getRendaMensalTotal() {
		return rendaMensalTotal;
	}

	public void setRendaMensalTotal(BigDecimal rendaMensalTotal) {
		this.rendaMensalTotal = rendaMensalTotal;
	}

	public List<CategoriaPersonalizadaDTO> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaPersonalizadaDTO> categorias) {
		this.categorias = categorias;
	}

}
