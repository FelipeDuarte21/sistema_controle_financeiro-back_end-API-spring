package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;

import br.com.felipeduarte.APIControleFinanceiro.model.LancamentoSalvo;
import io.swagger.annotations.ApiModelProperty;

public class LancamentoSalvoDTO {

	@ApiModelProperty(value = "Identificação do lançamento")
	private Long id;
	
	@ApiModelProperty(value = "nome do lançamento")
	private String nome;
	
	@ApiModelProperty(value = "descrição do lançamento")
	private String descricao;
	
	@ApiModelProperty(value = "valor do lançamento")
	private BigDecimal valor;
	
	@ApiModelProperty(value = "tipo do lançamento")
	private TipoLancamentoDTO tipo;
	
	private CategoriaDTO categoria;
	
	public LancamentoSalvoDTO() {
		
	}
	
	public LancamentoSalvoDTO(LancamentoSalvo lancamentoDTO) {
		this.id = lancamentoDTO.getId();
		this.nome = lancamentoDTO.getNome();
		this.descricao = lancamentoDTO.getDescricao();
		this.valor = lancamentoDTO.getValor();
		this.tipo = new TipoLancamentoDTO(lancamentoDTO.getTipo());
		this.categoria = new CategoriaDTO(lancamentoDTO.getCategoria());
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
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TipoLancamentoDTO getTipo() {
		return tipo;
	}

	public void setTipo(TipoLancamentoDTO tipo) {
		this.tipo = tipo;
	}

	public CategoriaDTO getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaDTO categoria) {
		this.categoria = categoria;
	}

}
