package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.enums.TipoLancamento;

@Entity
@Table(name = "lancamento")
public class Lancamento implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String titulo;
	private String descricao;
	private BigDecimal valor;
	
	@Column(name = "data_lancamento")
	private LocalDate dataLancamento;
	
	@Column(name = "data_registro")
	private LocalDateTime dataRegistro;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_folha")
	private Folha folha;
	
	@Enumerated(EnumType.ORDINAL)
	private TipoLancamento tipo;
	
	public Lancamento() {
		
	}
	
	public Lancamento(LancamentoDadosDTO lancamentoDTO) {
		this.titulo = lancamentoDTO.getTitulo();
		this.descricao = lancamentoDTO.getDescricao();
		this.valor = lancamentoDTO.getValor();
		this.dataLancamento = lancamentoDTO.getData();
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

	public LocalDate getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(LocalDate dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public LocalDateTime getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(LocalDateTime dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Folha getFolha() {
		return folha;
	}

	public void setFolha(Folha folha) {
		this.folha = folha;
	}

	public TipoLancamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lancamento other = (Lancamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
