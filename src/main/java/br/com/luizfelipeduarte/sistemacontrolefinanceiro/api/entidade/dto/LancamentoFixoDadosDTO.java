package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Lancamento;

public class LancamentoFixoDadosDTO {
	
	private String titulo;
	private String descricao;
	private BigDecimal valor;
	private Integer tipo;
	
	public LancamentoFixoDadosDTO() {
		
	}
	
	public LancamentoFixoDadosDTO(Lancamento lancamento) {
		this.titulo = lancamento.getTitulo();
		this.descricao = lancamento.getDescricao();
		this.valor = lancamento.getValor();
		this.tipo = lancamento.getTipo().getCodigo();
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

}
