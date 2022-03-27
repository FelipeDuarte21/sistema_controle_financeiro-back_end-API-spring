package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import br.com.felipeduarte.APIControleFinanceiro.model.TipoLancamento;
import io.swagger.annotations.ApiModelProperty;

public class TipoLancamentoDTO {
	
	@ApiModelProperty(value = "Valor do tipo de lançamento")
	private Integer valor;
	
	@ApiModelProperty(value = "Nome do tipo de lançamento")
	private String nome;
	
	public TipoLancamentoDTO() {
		
	}

	public TipoLancamentoDTO(Integer valor, String nome) {
		this.valor = valor;
		this.nome = nome;
	}
	
	public TipoLancamentoDTO(TipoLancamento tipo) {
		this.valor = tipo.getValor();
		this.nome = tipo.getNome();
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
