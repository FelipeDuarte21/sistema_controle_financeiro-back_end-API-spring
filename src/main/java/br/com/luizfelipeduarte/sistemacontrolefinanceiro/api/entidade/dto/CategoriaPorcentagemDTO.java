package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import javax.validation.constraints.NotNull;

public class CategoriaPorcentagemDTO {
	
	@NotNull(message = "Informe o id da categoria")
	private Long id;
	
	@NotNull(message = "Informe a porcentagem da categoria")
	private Double porcentagem;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Double getPorcentagem() {
		return porcentagem;
	}
	
	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

}