package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Lancamento;

public class LancamentoDTO {
	
	private Long id;
	
	private String titulo;
	
	private String descricao;
	
	private BigDecimal valor;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dataRegistro;
	
	private String tipo;
	
	public LancamentoDTO() {
		
	}
	
	public LancamentoDTO(Lancamento lancamento) {
		this.id = lancamento.getId();
		this.titulo = lancamento.getTitulo();
		this.descricao = lancamento.getDescricao();
		this.valor = lancamento.getValor();
		this.data = lancamento.getDataLancamento();
		this.dataRegistro = lancamento.getDataRegistro();
		this.tipo = lancamento.getTipo().getDescricao();
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public LocalDateTime getDataRegistro() {
		return dataRegistro;
	}
	
	public void setDataRegistro(LocalDateTime dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
