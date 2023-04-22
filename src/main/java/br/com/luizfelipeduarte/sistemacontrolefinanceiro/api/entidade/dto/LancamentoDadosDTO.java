package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LancamentoDadosDTO{
	
	@NotNull(message = "Informe o campo titulo")
	@Length(min=3,max=80,message = "O campo titulo deverá ter entre {min} e {max} caracteres!")
	private String titulo;
	
	private String descricao;
	
	@NotNull(message = "Informa o campo valor")
	private BigDecimal valor;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Informe a data do lançamento")
	private LocalDate data;
	
	@NotNull(message = "Informe o valor do tipo")
	private Integer tipo;

	private boolean salvar;
	
	public LancamentoDadosDTO() {
		
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public boolean isSalvar() {
		return salvar;
	}

	public void setSalvar(boolean salvar) {
		this.salvar = salvar;
	}

}
