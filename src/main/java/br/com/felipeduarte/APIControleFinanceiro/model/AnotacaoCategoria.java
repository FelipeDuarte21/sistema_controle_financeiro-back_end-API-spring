package br.com.felipeduarte.APIControleFinanceiro.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.AnotacaoCategoriaSalvarDTO;

@Entity
@Table(name = "anotacao_categoria")
public class AnotacaoCategoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "data_cadastro")
	private LocalDate data;
	
	private String titulo;
	
	private String descricao;
	
	private Boolean riscado;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_categoria")
	private Categoria categoria;
	
	public AnotacaoCategoria() {
		
	}

	public AnotacaoCategoria(Long id, LocalDate data, String titulo, String descricao, Boolean riscado) {
		this.id = id;
		this.data = data;
		this.titulo = titulo;
		this.descricao = descricao;
		this.riscado = riscado;
	}
	
	public AnotacaoCategoria(AnotacaoCategoriaSalvarDTO anotacaoDTO) {
		this.titulo = anotacaoDTO.getTitulo();
		this.descricao = anotacaoDTO.getDescricao();
		this.data = anotacaoDTO.getData();
		this.riscado = false;
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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
}
