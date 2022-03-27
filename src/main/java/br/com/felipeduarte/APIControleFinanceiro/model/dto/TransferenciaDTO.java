package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;

public class TransferenciaDTO {
	
	@ApiModelProperty(value = "Identificação da categoria que sai a transferência (origem)")
	@NotNull(message = "Informe o id da categoria de origem")
	private Long categoriaOrigem;
	
	@ApiModelProperty(value = "Identificação da categoria que recebe a transferência (destino)")
	@NotNull(message = "Informe o id da categoria de destino")
	private Long categoriaDestino;
	
	@ApiModelProperty(value = "Nome da transferência")
	@NotNull(message = "Informe o nome")
	@Length(min=3,max=80,message = "O campo nome deverá ter entre {min} e {max} caracteres!")
	private String nome;
	
	@ApiModelProperty(value = "Data da realização da transferência")
	@NotNull(message = "Informe a data")
	private LocalDate data;
	
	@ApiModelProperty(value = "Descrição da transferência")
	private String descricao;
	
	@ApiModelProperty(value = "Valor da transferência")
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
