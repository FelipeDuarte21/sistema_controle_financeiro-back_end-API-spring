package br.com.felipeduarte.APIControleFinanceiro.model.enums;

public enum TipoLancamentoEnum {
	
	PROVENTO(0,"Provento"),
	DESPESA(1,"Despesa");
	
	private Integer valor;
	private String nome;
	
	private TipoLancamentoEnum(Integer valor, String nome) {
		this.valor = valor;
		this.nome = nome;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
