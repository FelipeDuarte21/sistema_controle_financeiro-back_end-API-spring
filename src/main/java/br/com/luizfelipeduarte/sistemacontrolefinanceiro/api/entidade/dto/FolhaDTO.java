package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Folha;


public class FolhaDTO {

	private Long id;

	private Integer mes;
	
	private Integer ano;
	
	private BigDecimal saldoAnterior;
	
	private BigDecimal saldoAtual;
	
	private Boolean fechado;
	
	public FolhaDTO() {
		
	}

	public FolhaDTO(Folha folha) {
		this.id = folha.getId();
		this.ano = folha.getMesAno().getYear();
		this.mes = folha.getMesAno().getMonthValue();
		this.saldoAnterior = folha.getSaldoAnterior();
		this.saldoAtual = folha.getSaldoAtual();
		this.fechado = folha.getFechado();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public BigDecimal getSaldoAnterior() {
		return saldoAnterior;
	}
	
	public void setSaldoAnterior(BigDecimal saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}
	
	public BigDecimal getSaldoAtual() {
		return saldoAtual;
	}
	
	public void setSaldoAtual(BigDecimal saldoAtual) {
		this.saldoAtual = saldoAtual;
	}
	
	public Boolean getFechado() {
		return fechado;
	}
	
	public void setFechado(Boolean fechado) {
		this.fechado = fechado;
	}

}
