package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AnotacaoDadosDTO {
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Informe a data de cadastro")
	private LocalDate data;
	
	@NotNull(message = "Informe o titulo")
	@Size(max = 50, message = "Informa um titulo até {max} caracteres")
	private String titulo;
	
	@NotNull(message = "Informe uma descrição")
	private String descricao;
	
	public AnotacaoDadosDTO() {
		
	}
	
	public AnotacaoDadosDTO(LocalDate data, String titulo, String descricao) {
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
