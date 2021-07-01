package br.com.felipeduarte.APIControleFinanceiro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TipoLancamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Integer valor;
	private String nome;
	
	private List<Lancamento> lancamentos = new ArrayList<>();
	
	public TipoLancamento() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}
	
}
