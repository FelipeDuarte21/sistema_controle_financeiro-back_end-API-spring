package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;

public class ParceladoSalvarDTO {
	
	@NotNull(message="Informe um titulo")
	@Length(max = 50, message = "O campo titulo deve ter {max} caracteres")
	@ApiModelProperty(value = "titulo do parcelado")
	private String titulo;
	
	@NotNull(message = "Informe uma descrição")
	@ApiModelProperty(value = "descrição do parcelado")
	private String descricao;
	
	@NotNull(message = "Informe a data da realização do parcelado")
	@ApiModelProperty(value = "data de realização do parcelado")
	private LocalDate data;
	
	@NotNull(message = "Informe o total de parcelas do parcelado")
	@ApiModelProperty(value = "quantidade total de parcelas do parcelado")
	private Integer totalParcela;
	
	@NotNull(message = "Informe a data do vencimento da primeira parcela do parcelado")
	@ApiModelProperty(value = "data do vencimento da primeira parcela do parcelado")
	private LocalDate dataVencimentoPrimeiraParcela;
	
	@NotNull(message = "Informe um valor da parcela")
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
