package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.enums;

import java.math.BigDecimal;

public enum TipoCalculo {
	
	SOMAR(0,"Soma") {
		@Override
		public BigDecimal calcular(BigDecimal saldoAtual, BigDecimal valor) {
			return saldoAtual.add(valor);
		}
	},
	DIMINUIR(1,"Diminuir") {
		@Override
		public BigDecimal calcular(BigDecimal saldoAtual, BigDecimal valor) {
			return saldoAtual.subtract(valor);
		}
	};

	private Integer codigo;
	private String operacao;
	
	private TipoCalculo(Integer codigo, String operacao) {
		this.codigo = codigo;
		this.operacao = operacao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	
	public abstract BigDecimal calcular(BigDecimal saldoAtual, BigDecimal valor);

}
