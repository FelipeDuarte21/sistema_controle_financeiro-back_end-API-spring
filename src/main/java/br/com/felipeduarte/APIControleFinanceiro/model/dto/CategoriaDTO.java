package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.time.LocalDate;

import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import io.swagger.annotations.ApiModelProperty;

public class CategoriaDTO {
	
	@ApiModelProperty(value = "Identificação da categoria")
	private Long id;
	
	@ApiModelProperty(value = "Nome da categoria")
	private String nome;
	
	@ApiModelProperty(value = "Descrição da categoria")
	private String descricao;
	
	@ApiModelProperty(value = "Data de cadastro da categoria")
	private LocalDate dataCadastro;
	
	public CategoriaDTO() {
		
	}

	public CategoriaDTO(Long id, String nome, String descricao, LocalDate dataCadastro) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.dataCadastro = dataCadastro;
	}

	public CategoriaDTO(Categoria categoria) {
		this.id = categoria.getId();
		this.nome = categoria.getNome();
		this.descricao = categoria.getDescricao();
		this.dataCadastro = categoria.getDataCadastro();
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
	
}
