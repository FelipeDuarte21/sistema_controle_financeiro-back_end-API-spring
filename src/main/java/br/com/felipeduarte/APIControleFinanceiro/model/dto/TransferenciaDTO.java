package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class TransferenciaDTO {
	
	@NotNull(message = "Informe o campo idCategoriaOrigem")
	public Long idCategoriaOrigem;
	
	@NotNull(message = "Informe o campo idCategoriaDestino")
	public Long idCategoriaDestino;
	
	@NotNull(message = "Informe o campo nome")
	@Length(min=3,max=80,message = "O campo nome deverá ter entre {min} e {max} caracteres!")
	public String nome;
	
	public String descricao;
	
	@NotNull(message = "Informe o campo valor")
	public Double valor;
	
	public TransferenciaDTO() {
		
	}

	public Long getIdCategoriaOrigem() {
		return idCategoriaOrigem;
	}

	public void setIdCategoriaOrigem(Long idCategoriaOrigem) {
		this.idCategoriaOrigem = idCategoriaOrigem;
	}

	public Long getIdCategoriaDestino() {
		return idCategoriaDestino;
	}

	public void setIdCategoriaDestino(Long idCategoriaDestino) {
		this.idCategoriaDestino = idCategoriaDestino;
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

}
