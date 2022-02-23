package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.time.YearMonth;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import io.swagger.annotations.ApiModelProperty;

public class BalancoFaixaDTO {
	
	@ApiModelProperty(value = "Identificação do balanço")
	private Long id;
	
	@ApiModelProperty(value = "Data (ano e mês) do balanço")
	private YearMonth mesAno;
	
	@ApiModelProperty(value = "Balanço central da busca")
	private Boolean atual;

	public BalancoFaixaDTO() {
		
	}
	
	public BalancoFaixaDTO(Balanco balanco) {
		this.id = balanco.getId();
		this.mesAno = balanco.getMesAno();
		this.atual = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public YearMonth getMesAno() {
		return mesAno;
	}

	public void setMesAno(YearMonth mesAno) {
		this.mesAno = mesAno;
	}

	public Boolean getAtual() {
		return atual;
	}

	public void setAtual(Boolean atual) {
		this.atual = atual;
	}

}
