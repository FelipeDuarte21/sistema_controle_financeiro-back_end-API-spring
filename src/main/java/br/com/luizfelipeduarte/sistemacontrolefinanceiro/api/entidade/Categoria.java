package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.CategoriaDadosDTO;

@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	private String descricao;
	private LocalDate dataCadastro;
	private Double porcentagem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_conta")
	private Conta conta;
	
	@OneToMany(mappedBy = "categoria",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Folha> folhas = new ArrayList<>();
	
	@OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<LancamentoFixo> lancamentosFixos = new ArrayList<>();
	
	@OneToMany(mappedBy = "categoria",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Anotacao> anotações = new ArrayList<>();
	
	@OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Parcelado> parcelados = new ArrayList<>();
	
	public Categoria() {
		
	}
	
	public Categoria(String nome, String descricao, LocalDate dataCadastro, Double porcentagem, Conta conta) {
		this.nome = nome;
		this.descricao = descricao;
		this.dataCadastro = dataCadastro;
		this.porcentagem = porcentagem;
		this.conta = conta;
	}

	public Categoria(CategoriaDadosDTO categoriaDadosDTO) {
		this.nome = categoriaDadosDTO.getNome();
		this.descricao = categoriaDadosDTO.getDescricao();
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

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public List<Folha> getFolhas() {
		return folhas;
	}

	public void setFolhas(List<Folha> folhas) {
		this.folhas = folhas;
	}

	public List<Anotacao> getAnotações() {
		return anotações;
	}

	public void setAnotações(List<Anotacao> anotações) {
		this.anotações = anotações;
	}

	public List<Parcelado> getParcelados() {
		return parcelados;
	}

	public void setParcelados(List<Parcelado> parcelados) {
		this.parcelados = parcelados;
	}

	public List<LancamentoFixo> getLancamentosFixos() {
		return lancamentosFixos;
	}

	public void setLancamentosFixos(List<LancamentoFixo> lancamentosFixos) {
		this.lancamentosFixos = lancamentosFixos;
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
		Categoria other = (Categoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
