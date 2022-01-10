package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;

public class BalancoResumoDTO {
	
	private Long id;
	private Integer mes;
	private Integer ano;
	private Boolean atual;

	public BalancoResumoDTO() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
	public Boolean getAtual() {
		return atual;
	}

	public void setAtual(Boolean atual) {
		this.atual = atual;
	}

	public static BalancoResumoDTO converteBalancoParaBalancoResumoDTO(Balanco b) {
		BalancoResumoDTO bdto = new BalancoResumoDTO();
		bdto.setId(b.getId());
		bdto.setAno(b.getAno());
		bdto.setMes(b.getMes());
		return bdto;
	}
	
}
