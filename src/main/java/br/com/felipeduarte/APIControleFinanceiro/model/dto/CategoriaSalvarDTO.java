package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;

public class CategoriaSalvarDTO {

	@ApiModelProperty(value = "Nome da categoria")
	@NotNull(message = "Informe o campo nome")
	@Length(min=3,max=60, message = "O campo nome deve ter entre {min} e {max} caracteres")
	private String nome;
	
	@ApiModelProperty(value = "Descrição da categoria")
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
