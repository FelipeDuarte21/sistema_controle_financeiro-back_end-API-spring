package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class ParcelaPagarDTO {
	
	@NotNull(message = "Informe um valor da parcela")
	@ApiModelProperty(value = "Valor da parcela")
	private BigDecimal valor;
	
	@NotNull(message = "Informe a data de vencimento da parcela")
	@ApiModelProperty(value = "data de vencimento da parcela")
	private LocalDate dataVencimento;
	
	@NotNull(message = "Informe a data de pagamento da parcela")
	@ApiModelProperty(value = "data de pagamento da parcela")
	private LocalDate dataPagamento;
	
	public ParcelaPagarDTO() {
		
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
	
	

}
