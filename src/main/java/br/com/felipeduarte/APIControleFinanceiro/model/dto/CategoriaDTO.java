package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class CategoriaDTO {
	
	private Long id;
	
	@NotNull(message = "Informe o campo nome")
	@Length(min=3,max=60, message = "O campo nome deve ter entre {min} e {max} caracteres")
	private String nome;
	
	private String descricao;
	
	@NotNull(message = "Informe o campo data cadastro")
	private LocalDate dataCadastro;

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
