package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class TransferenciaDTO {
	
	@NotNull(message = "Informe o id da categoria de origem")
	public Long categoriaOrigem;
	
	@NotNull(message = "Informe o id da categoria de destino")
	public Long categoriaDestino;
	
	@NotNull(message = "Informe o nome")
	@Length(min=3,max=80,message = "O campo nome deverá ter entre {min} e {max} caracteres!")
	public String nome;
	
	public String descricao;
	
	@NotNull(message = "Informe o valor")
	public BigDecimal valor;
	
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
