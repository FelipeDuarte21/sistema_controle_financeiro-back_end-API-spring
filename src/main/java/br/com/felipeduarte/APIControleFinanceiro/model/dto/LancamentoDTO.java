package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;
import io.swagger.annotations.ApiModelProperty;

public class LancamentoDTO {
	
	@ApiModelProperty(value = "Identificação do lançamento")
	private Long id;
	
	@ApiModelProperty(value = "Nome do lançamento")
	private String nome;
	
	@ApiModelProperty(value = "Descrição do lançamento")
	private String descricao;
	
	@ApiModelProperty(value = "Valor do lançamento")
	private BigDecimal valor;
	
	@ApiModelProperty(value = "Data que ocorreu o lançamento")
	private LocalDate data;
	
	@ApiModelProperty(value = "Data que foi registrado o lançamento")
	private LocalDateTime dataRegistro;
	
	@ApiModelProperty(value = "Tipo do lançamento (Provento ou Despesa)")
	private TipoLancamentoDTO tipo;
	
	@ApiModelProperty(value = "Balanço que pertence o lançamento")
	private BalancoDTO balanco;
	
	public LancamentoDTO() {
		
	}
	
	public LancamentoDTO(Long id, String nome, String descricao, BigDecimal valor, LocalDate data,
			LocalDateTime dataRegistro, TipoLancamentoDTO tipo, BalancoDTO balanco) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.valor = valor;
		this.data = data;
		this.dataRegistro = dataRegistro;
		this.tipo = tipo;
		this.balanco = balanco;
	}
	
	public LancamentoDTO(Lancamento lancamento) {
		this.id = lancamento.getId();
		this.nome = lancamento.getNome();
		this.descricao = lancamento.getDescricao();
		this.valor = lancamento.getValor();
		this.data = lancamento.getDataLancamento();
		this.dataRegistro = lancamento.getDataRegistro();
		this.tipo = new TipoLancamentoDTO(lancamento.getTipo());
		this.balanco = new BalancoDTO(lancamento.getBalanco());
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
	
	public LocalDate getData() {
		return data;
	}
	
	public void setData(LocalDate data) {
		this.data = data;
	}
	
	public LocalDateTime getDataRegistro() {
		return dataRegistro;
	}
	
	public void setDataRegistro(LocalDateTime dataRegistro) {
		this.dataRegistro = dataRegistro;
	}
	
	public TipoLancamentoDTO getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoLancamentoDTO tipo) {
		this.tipo = tipo;
	}

	public BalancoDTO getBalanco() {
		return balanco;
	}

	public void setBalanco(BalancoDTO balanco) {
		this.balanco = balanco;
	}

}
