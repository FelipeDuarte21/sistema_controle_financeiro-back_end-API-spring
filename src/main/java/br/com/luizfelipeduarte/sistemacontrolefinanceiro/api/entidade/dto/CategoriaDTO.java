package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Categoria;

public class CategoriaDTO {
	
	private Long id;
	private String nome;
	private String descricao;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataCadastro;
	
	private Double porcentagem;
	
	public CategoriaDTO() {
		
	}

	public CategoriaDTO(Long id, String nome, String descricao, LocalDate dataCadastro,Double porcentagem) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.dataCadastro = dataCadastro;
		this.porcentagem = porcentagem;
	}

	public CategoriaDTO(Categoria categoria) {
		this.id = categoria.getId();
		this.nome = categoria.getNome();
		this.descricao = categoria.getDescricao();
		this.dataCadastro = categoria.getDataCadastro();
		this.porcentagem = categoria.getPorcentagem();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}
	
}
