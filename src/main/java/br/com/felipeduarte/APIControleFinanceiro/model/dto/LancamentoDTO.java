package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class LancamentoDTO {
	
	private Long id;
	
	@NotNull(message = "Informe o campo nome")
	@Length(min=3,max=80,message = "O campo nome deverá ter entre {min} e {max} caracteres!")
	private String nome;
	
	private String descricao;
	
	@NotNull(message = "Informa o campo valor")
	private Double valor;
	
	@NotNull(message = "Infome o campo data de cadastro")
	private LocalDate dataCadastro;
	
	private Boolean sugestao;
	
	@NotNull(message = "Informe o id do balanco")
	private Long balanco;
	
	@NotNull(message = "Informe o valor do tipo")
	private Integer tipo;

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

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Boolean getSugestao() {
		return sugestao;
	}

	public void setSugestao(Boolean sugestao) {
		this.sugestao = sugestao;
	}

	public Long getBalanco() {
		return balanco;
	}

	public void setBalanco(Long balanco) {
		this.balanco = balanco;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

}
