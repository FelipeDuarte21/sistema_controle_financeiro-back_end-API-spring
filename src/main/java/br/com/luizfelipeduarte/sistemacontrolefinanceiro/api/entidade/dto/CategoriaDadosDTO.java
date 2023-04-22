package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class CategoriaDadosDTO {

	@NotNull(message = "Informe o id da conta")
	private Long conta;
	
	@NotNull(message = "Informe o campo nome")
	@Length(min=3,max=60, message = "O campo nome deve ter entre {min} e {max} caracteres")
	private String nome;
	
	private String descricao;
	
	public CategoriaDadosDTO() {
		
	}
	
	public CategoriaDadosDTO(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}

	public Long getConta() {
		return conta;
	}

	public void setConta(Long conta) {
		this.conta = conta;
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
