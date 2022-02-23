package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import io.swagger.annotations.ApiModelProperty;

public class BalancoDTO {

	@ApiModelProperty(value = "Identificação do balanço")
	private Long id;
	
	@ApiModelProperty(value = "Data (ano e mês) do balanço")
	private YearMonth mesAno;
	
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
		this.mesAno = mesAno;
		this.saldoAnterior = saldoAnterior;
		this.saldoAtual = saldoAtual;
		this.fechado = fechado;
	}

	public BalancoDTO(Balanco balanco) {
		this.id = balanco.getId();
		this.mesAno = balanco.getMesAno();
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

	public YearMonth getMesAno() {
		return mesAno;
	}
	
	public void setMesAno(YearMonth mesAno) {
		this.mesAno = mesAno;
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
