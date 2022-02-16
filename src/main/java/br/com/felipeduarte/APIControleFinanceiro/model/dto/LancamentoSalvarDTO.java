package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class LancamentoSalvarDTO {
	
	@NotNull(message = "Informe o campo nome")
	@Length(min=3,max=80,message = "O campo nome deverá ter entre {min} e {max} caracteres!")
	private String nome;
	
	private String descricao;
	
	@NotNull(message = "Informa o campo valor")
	private BigDecimal valor;
	
	@NotNull(message = "Informe a data do lançamento")
	private LocalDate data;
	
	@NotNull(message = "Informe o valor do tipo")
	private Integer tipo;
	
	public LancamentoSalvarDTO() {
		
	}

	public LancamentoSalvarDTO(String nome,String descricao, BigDecimal valor,LocalDate data,Integer tipo) {
		this.nome = nome;
		this.descricao = descricao;
		this.valor = valor;
		this.data = data;
		this.tipo = tipo;
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

}
