package br.com.felipeduarte.APIControleFinanceiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "parcela")
public class Parcela implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "numero")
	private Integer numero;
	
	@Column(name = "valor")
	private BigDecimal valor;
	
	@Column(name = "data_vencimento")
	private LocalDate dataVencimento;
	
	@Column(name = "data_pagamento")
	private LocalDate dataPagamento;
	
	@Column(name = "pago")
	private Boolean pago;
	
	@Column(name = "primeiro")
	private Boolean primeiro;
	
	@Column(name = "ultimo")
	private Boolean ultimo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_parcelado")
	private Parcelado parcelado;
	
	public Parcela() {
		
	}

	public Parcela(Long id, Integer numero, BigDecimal valor, LocalDate dataVencimento, LocalDate dataPagamento,
			Boolean pago, Boolean primeiro, Boolean ultimo) {
		this.id = id;
		this.numero = numero;
		this.valor = valor;
		this.dataVencimento = dataVencimento;
		this.dataPagamento = dataPagamento;
		this.pago = pago;
		this.primeiro = primeiro;
		this.ultimo = ultimo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Boolean getPago() {
		return pago;
	}

	public void setPago(Boolean pago) {
		this.pago = pago;
	}

	public Boolean getPrimeiro() {
		return primeiro;
	}

	public void setPrimeiro(Boolean primeiro) {
		this.primeiro = primeiro;
	}

	public Boolean getUltimo() {
		return ultimo;
	}

	public void setUltimo(Boolean ultimo) {
		this.ultimo = ultimo;
	}

	public Parcelado getParcelado() {
		return parcelado;
	}

	public void setParcelado(Parcelado parcelado) {
		this.parcelado = parcelado;
	}

}
