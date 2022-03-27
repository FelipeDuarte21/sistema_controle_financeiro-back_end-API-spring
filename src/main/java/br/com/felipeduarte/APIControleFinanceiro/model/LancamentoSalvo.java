package br.com.felipeduarte.APIControleFinanceiro.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoSalvoSalvarDTO;

@Entity
@Table(name = "lancamento_salvo")
public class LancamentoSalvo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "valor")
	private BigDecimal valor;
	
	@ManyToOne
	@JoinColumn(name = "id_tipo")
	private TipoLancamento tipo;
	
	@ManyToOne
	@JoinColumn(name = "id_categoria")
	private Categoria categoria;
	
	public LancamentoSalvo() {
		
	}

	public LancamentoSalvo(Long id, String nome, String descricao, BigDecimal valor) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.valor = valor;
	}
	
	public LancamentoSalvo(LancamentoSalvarDTO lancamentoDTO) {
		this.nome = lancamentoDTO.getNome();
		this.descricao = lancamentoDTO.getDescricao();
		this.valor = lancamentoDTO.getValor();
	}
	
	public LancamentoSalvo(LancamentoSalvoSalvarDTO lancamentoDTO) {
		this.nome = lancamentoDTO.getNome();
		this.descricao = lancamentoDTO.getDescricao();
		this.valor = lancamentoDTO.getValor();
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

	public TipoLancamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}
