package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Conta;

public class ContaDTO {
	
	private Long id;
	private BigDecimal rendaMensalTotal;
	
	public ContaDTO() {
		
	}
	
	public ContaDTO(Conta conta) {
		this.id = conta.getId();
		this.rendaMensalTotal = conta.getRendaMensalTotal();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getRendaMensalTotal() {
		return rendaMensalTotal;
	}
	
	public void setRendaMensalTotal(BigDecimal rendaMensalTotal) {
		this.rendaMensalTotal = rendaMensalTotal;
	}

}
