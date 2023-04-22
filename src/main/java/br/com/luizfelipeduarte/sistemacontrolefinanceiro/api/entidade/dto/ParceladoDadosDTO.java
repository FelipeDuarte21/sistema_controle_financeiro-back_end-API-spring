package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ParceladoDadosDTO {
	
	@NotNull(message="Informe um titulo")
	@Length(max = 50, message = "O campo titulo deve ter {max} caracteres")
	private String titulo;
	
	@NotNull(message = "Informe uma descrição")
	private String descricao;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Informe a data da realização do parcelado")
	private LocalDate data;
	
	@NotNull(message = "Informe o total de parcelas do parcelado")
	private Integer totalParcela;
	
	private ParcelaDadosDTO parcelaDados;
	
	public ParceladoDadosDTO() {
		
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

	public ParcelaDadosDTO getParcelaDados() {
		return parcelaDados;
	}

	public void setParcelaDados(ParcelaDadosDTO parcelaDados) {
		this.parcelaDados = parcelaDados;
	}

}
