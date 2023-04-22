package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.LancamentoFixo;

public class LancamentoFixoDTO {

	private Long id;

	private String titulo;
	
	private String descricao;
	
	private BigDecimal valor;
	
	private String tipo;
	
	public LancamentoFixoDTO() {
		
	}
	
	public LancamentoFixoDTO(LancamentoFixo lancamentoFixoDTO) {
		this.id = lancamentoFixoDTO.getId();
		this.titulo = lancamentoFixoDTO.getTitulo();
		this.descricao = lancamentoFixoDTO.getDescricao();
		this.valor = lancamentoFixoDTO.getValor();
		this.tipo = lancamentoFixoDTO.getTipo().getDescricao();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
