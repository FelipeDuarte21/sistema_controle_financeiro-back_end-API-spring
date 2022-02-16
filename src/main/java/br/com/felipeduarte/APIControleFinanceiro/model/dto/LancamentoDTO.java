package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;

public class LancamentoDTO {
	
	private Long id;
	private String nome;
	private String descricao;
	private BigDecimal valor;
	private LocalDate data;
	private LocalDateTime dataRegistro;
	private TipoLancamentoDTO tipo;
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
