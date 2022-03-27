package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

public class AnotacaoCategoriaSalvarDTO {
	
	@NotNull(message = "Informe a data de cadastro")
	@ApiModelProperty(value = "Data de cadastro da anotação")
	private LocalDate data;
	
	@NotNull(message = "Informe o titulo")
	@Size(max = 50, message = "Informa um titulo até {max} caracteres")
	@ApiModelProperty(value = "Titulo da anotação")
	private String titulo;
	
	@NotNull(message = "Informe uma descrição")
	@ApiModelProperty(value = "Descrição da anotação")
	private String descricao;
	
	public AnotacaoCategoriaSalvarDTO() {
		
	}
	
	public AnotacaoCategoriaSalvarDTO(LocalDate data, String titulo, String descricao) {
		this.data = data;
		this.titulo = titulo;
		this.descricao = descricao;
	}

	public LocalDate getData() {
		return data;
	}
	
	public void setData(LocalDate data) {
		this.data = data;
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

}
