package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Parcela;

public class ParcelaDTO {
	
	private Long id;

	private Integer numero;
	
	private BigDecimal valor;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimento;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamento;
	
	private Boolean pago;
	
	public ParcelaDTO() {
		
	}
	
	public ParcelaDTO(Parcela parcela) {
		this.id = parcela.getId();
		this.numero = parcela.getNumero();
		this.valor = parcela.getValor();
		this.dataVencimento = parcela.getDataVencimento();
		this.dataPagamento = parcela.getDataPagamento();
		this.pago = parcela.getPago();
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

}
