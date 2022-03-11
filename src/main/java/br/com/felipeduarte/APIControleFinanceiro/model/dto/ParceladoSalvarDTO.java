package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.annotations.ApiModelProperty;

public class ParceladoSalvarDTO {
	
	@ApiModelProperty(value = "titulo do parcelado")
	private String titulo;
	
	@ApiModelProperty(value = "descrição do parcelado")
	private String descricao;
	
	@ApiModelProperty(value = "data de realização do parcelado")
	private LocalDate data;
	
	@ApiModelProperty(value = "quantidade total de parcelas do parcelado")
	private Integer totalParcela;
	
	@ApiModelProperty(value = "data do vencimento da primeira parcela do parcelado")
	private LocalDate dataVencimentoPrimeiraParcela;
	
	@ApiModelProperty(value = "valor das parcelas do parcelado")
	private BigDecimal valor;
	
	public ParceladoSalvarDTO() {
		
	}

	public ParceladoSalvarDTO(String titulo, String descricao, LocalDate data, Integer totalParcela,
			LocalDate dataVencimentoPrimeiraParcela, BigDecimal valor) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.data = data;
		this.totalParcela = totalParcela;
		this.dataVencimentoPrimeiraParcela = dataVencimentoPrimeiraParcela;
		this.valor = valor;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Integer getTotalParcela() {
		return totalParcela;
	}

	public void setTotalParcela(Integer totalParcela) {
		this.totalParcela = totalParcela;
	}

	public LocalDate getDataVencimentoPrimeiraParcela() {
		return dataVencimentoPrimeiraParcela;
	}

	public void setDataVencimentoPrimeiraParcela(LocalDate dataVencimentoPrimeiraParcela) {
		this.dataVencimentoPrimeiraParcela = dataVencimentoPrimeiraParcela;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
