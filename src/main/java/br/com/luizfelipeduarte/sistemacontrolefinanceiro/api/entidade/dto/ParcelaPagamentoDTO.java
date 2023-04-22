package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

public class ParcelaPagamentoDTO {
	
	@NotNull(message = "Informe um valor da parcela")
	private BigDecimal valor;
	
	@NotNull(message = "Informe a data de vencimento da parcela")
	private LocalDate dataVencimento;
	
	@NotNull(message = "Informe a data de pagamento da parcela")
	private LocalDate dataPagamento;
	
	public ParcelaPagamentoDTO() {
		
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
