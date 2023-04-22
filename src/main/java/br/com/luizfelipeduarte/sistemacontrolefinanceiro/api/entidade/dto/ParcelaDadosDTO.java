package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ParcelaDadosDTO {
	
	@NotNull(message = "Informe um valor da parcela")
	private BigDecimal valor;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Informe a data do vencimento da primeira parcela do parcelado")
	private LocalDate dataVencimentoPrimeiraParcela;

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDate getDataVencimentoPrimeiraParcela() {
		return dataVencimentoPrimeiraParcela;
	}

	public void setDataVencimentoPrimeiraParcela(LocalDate dataVencimentoPrimeiraParcela) {
		this.dataVencimentoPrimeiraParcela = dataVencimentoPrimeiraParcela;
	}

}
