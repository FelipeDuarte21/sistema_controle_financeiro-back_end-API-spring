package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import javax.validation.constraints.NotNull;

public class CategoriaPersonalizadaDTO {

	@NotNull(message = "Informe o campo nome")
	private String nome;
	
	private String descricao;
	
	@NotNull(message = "Informe o campo porcentagem")
	private Double porcentagem;
	
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
	
	public Double getPorcentagem() {
		return porcentagem;
	}
	
	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}
	
}
