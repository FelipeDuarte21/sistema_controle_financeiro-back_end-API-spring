package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import io.swagger.annotations.ApiModelProperty;

public class BalancoDTO {

	@ApiModelProperty(value = "Identificação do balanço")
	private Long id;

	@ApiModelProperty(value = "Mês do balanço")
	private Integer mes;
	
	@ApiModelProperty(value = "Ano do balanço")
	private Integer ano;
	
	@ApiModelProperty(value = "Saldo do balanço anterior (mês anterior)")
	private BigDecimal saldoAnterior;
	
	@ApiModelProperty(value = "Saldo do balanço")
	private BigDecimal saldoAtual;
	
	@ApiModelProperty(value = "Balanço em aberto")
	private Boolean fechado;
	
	@ApiModelProperty(value = "Categoria que o balanço pertence")
	private CategoriaDTO categoria;
	
	public BalancoDTO() {
		
	}
	
	public BalancoDTO(Long id, YearMonth mesAno, BigDecimal saldoAnterior, BigDecimal saldoAtual, 
			Boolean fechado) {
		this.id = id;
		this.ano = mesAno.getYear();
		this.mes = mesAno.getMonthValue();
		this.saldoAnterior = saldoAnterior;
		this.saldoAtual = saldoAtual;
		this.fechado = fechado;
	}

	public BalancoDTO(Balanco balanco) {
		this.id = balanco.getId();
		this.ano = balanco.getMesAno().getYear();
		this.mes = balanco.getMesAno().getMonthValue();
		this.saldoAnterior = balanco.getSaldoAnterior();
		this.saldoAtual = balanco.getSaldoAtual();
		this.fechado = balanco.getFechado();
		this.categoria = new CategoriaDTO(balanco.getCategoria());
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

	public BigDecimal getSaldoAnterior() {
		return saldoAnterior;
	}
	
	public void setSaldoAnterior(BigDecimal saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}
	
	public BigDecimal getSaldoAtual() {
		return saldoAtual;
	}
	
	public void setSaldoAtual(BigDecimal saldoAtual) {
		this.saldoAtual = saldoAtual;
	}
	
	public Boolean getFechado() {
		return fechado;
	}
	
	public void setFechado(Boolean fechado) {
		this.fechado = fechado;
	}
	
	public CategoriaDTO getCategoria() {
		return categoria;
	}
	
	public void setCategoria(CategoriaDTO categoria) {
		this.categoria = categoria;
	}

}
