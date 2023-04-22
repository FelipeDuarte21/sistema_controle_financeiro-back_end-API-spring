package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.enums;


public enum TipoLancamento {
	
	PROVENTO(0,"Provento") {
		@Override
		public TipoCalculo getTipoCalculo() {
			return TipoCalculo.SOMAR;
		}
	},
	DESPESA(1, "Despesa") {
		@Override
		public TipoCalculo getTipoCalculo() {
			return TipoCalculo.DIMINUIR;
		}
	};
	
	private Integer codigo;
	private String descricao;
	
	private TipoLancamento(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public static TipoLancamento toEnum(Integer codigo) {
		
		if(codigo == null) {
			return null;
		}
		
		for(TipoLancamento x: TipoLancamento.values()) {
			if(codigo.equals(x.getCodigo())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Tipo de Lancamento Inválido!");
	}
	
	public abstract TipoCalculo getTipoCalculo();
	
	public boolean isProvento() {
		return this == PROVENTO;
	}
	
	public boolean isDespesa() {
		return this == DESPESA;
	}

}
