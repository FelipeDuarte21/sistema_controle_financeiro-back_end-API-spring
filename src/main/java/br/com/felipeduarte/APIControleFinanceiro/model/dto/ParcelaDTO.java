package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.felipeduarte.APIControleFinanceiro.model.Parcela;

public class ParcelaDTO {
	
	private Long id;
	private Integer numero;
	private BigDecimal valor;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimento;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamento;
	
	private Boolean pago;
	private Boolean primeiro;
	private Boolean ultimo;
	
	public ParcelaDTO() {
		
	}
	
	public ParcelaDTO(Long id, Integer numero, BigDecimal valor, LocalDate dataVencimento, LocalDate dataPagamento,
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
	
	public ParcelaDTO(Parcela parcela) {
		this.id = parcela.getId();
		this.numero = parcela.getNumero();
		this.valor = parcela.getValor();
		this.dataVencimento = parcela.getDataVencimento();
		this.dataPagamento = parcela.getDataPagamento();
		this.pago = parcela.getPago();
		this.primeiro = parcela.getPrimeiro();
		this.ultimo = parcela.getUltimo();
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

}
