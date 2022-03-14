package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;

import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;
import io.swagger.annotations.ApiModelProperty;

public class LancamentoSalvoSalvarDTO {
	
	@ApiModelProperty(value = "nome do lançamento")
	private String nome;
	
	@ApiModelProperty(value = "descrição do lançamento")
	private String descricao;
	
	@ApiModelProperty(value = "valor do lançamento")
	private BigDecimal valor;
	
	@ApiModelProperty(value = "tipo do lançamento")
	private Integer tipo;
	
	public LancamentoSalvoSalvarDTO() {
		
	}
	
	public LancamentoSalvoSalvarDTO(Lancamento lancamento) {
		this.nome = lancamento.getNome();
		this.descricao = lancamento.getDescricao();
		this.valor = lancamento.getValor();
		this.tipo = lancamento.getTipo().getValor();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

}
