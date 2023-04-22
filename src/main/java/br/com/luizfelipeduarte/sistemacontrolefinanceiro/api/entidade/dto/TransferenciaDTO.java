package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TransferenciaDTO {
	
	@NotNull(message = "Informe o id da categoria de origem")
	private Long categoriaOrigem;

	@NotNull(message = "Informe o id da categoria de destino")
	private Long categoriaDestino;

	@NotNull(message = "Informe o titulo")
	@Length(min=3,max=80,message = "O campo titulo deverá ter entre {min} e {max} caracteres!")
	private String titulo;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Informe a data")
	private LocalDate data;
	
	private String descricao;
	
	@NotNull(message = "Informe o valor")
	private BigDecimal valor;
	
	public TransferenciaDTO() {
		
	}

	public Long getCategoriaOrigem() {
		return categoriaOrigem;
	}

	public void setCategoriaOrigem(Long categoriaOrigem) {
		this.categoriaOrigem = categoriaOrigem;
	}

	public Long getCategoriaDestino() {
		return categoriaDestino;
	}

	public void setCategoriaDestino(Long categoriaDestino) {
		this.categoriaDestino = categoriaDestino;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
