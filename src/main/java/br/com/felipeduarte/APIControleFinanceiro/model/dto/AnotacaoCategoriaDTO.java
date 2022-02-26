package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.felipeduarte.APIControleFinanceiro.model.AnotacaoCategoria;
import io.swagger.annotations.ApiModelProperty;

public class AnotacaoCategoriaDTO {
	
	@ApiModelProperty(value = "Identificação da anotação")
	private Long id;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "Data de cadastro da anotação")
	private LocalDate data;
	
	@ApiModelProperty(value = "Titulo da anotação")
	private String titulo;
	
	@ApiModelProperty(value = "Descrição da anotação")
	private String descricao;
	
	@ApiModelProperty(value = "Anotação a ser ou não ser considerada")
	private Boolean riscado;
	
	@ApiModelProperty(value = "categoria da anotação")
	private CategoriaDTO categoria;
	
	public AnotacaoCategoriaDTO() {
		
	}

	public AnotacaoCategoriaDTO(Long id, LocalDate data, String titulo, String descricao, Boolean riscado,
			CategoriaDTO categoria) {
		this.id = id;
		this.data = data;
		this.titulo = titulo;
		this.descricao = descricao;
		this.riscado = riscado;
		this.categoria = categoria;
	}
	
	public AnotacaoCategoriaDTO(AnotacaoCategoria anotacao) {
		this.id = anotacao.getId();
		this.data = anotacao.getData();
		this.titulo = anotacao.getTitulo();
		this.descricao = anotacao.getDescricao();
		this.riscado = anotacao.getRiscado();
		this.categoria = new CategoriaDTO(anotacao.getCategoria());
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

	public CategoriaDTO getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaDTO categoria) {
		this.categoria = categoria;
	}

}
