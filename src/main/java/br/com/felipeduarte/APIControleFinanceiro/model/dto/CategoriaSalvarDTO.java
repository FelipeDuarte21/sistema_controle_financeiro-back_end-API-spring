package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class CategoriaSalvarDTO {

	@NotNull(message = "Informe o campo nome")
	@Length(min=3,max=60, message = "O campo nome deve ter entre {min} e {max} caracteres")
	private String nome;
	
	private String descricao;
	
	public CategoriaSalvarDTO() {
		
	}
	
	public CategoriaSalvarDTO(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
