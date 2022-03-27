package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import io.swagger.annotations.ApiModelProperty;

public class BalancoFaixaDTO {
	
	@ApiModelProperty(value = "Identificação do balanço")
	private Long id;
	
	@ApiModelProperty(value = "Mês do balanço")
	private Integer mes;
	
	@ApiModelProperty(value = "Ano do balanço")
	private Integer ano;
	
	@ApiModelProperty(value = "Balanço central da busca")
	private Boolean atual;

	public BalancoFaixaDTO() {
		
	}
	
	public BalancoFaixaDTO(Balanco balanco) {
		this.id = balanco.getId();
		this.ano = balanco.getMesAno().getYear();
		this.mes = balanco.getMesAno().getMonthValue();
		this.atual = false;
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

}
