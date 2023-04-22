package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Anotacao;

public class AnotacaoDTO {
	
	private Long id;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	
	private String titulo;
	
	private String descricao;
	
	private Boolean riscado;
	
	public AnotacaoDTO() {
		
	}

	public AnotacaoDTO(Anotacao anotacao) {
		this.id = anotacao.getId();
		this.data = anotacao.getData();
		this.titulo = anotacao.getTitulo();
		this.descricao = anotacao.getDescricao();
		this.riscado = anotacao.getRiscado();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Boolean getRiscado() {
		return riscado;
	}

	public void setRiscado(Boolean riscado) {
		this.riscado = riscado;
	}
	
}
