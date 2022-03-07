package br.com.felipeduarte.APIControleFinanceiro.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.ParceladoSalvarDTO;

@Entity
@Table(name = "parcelado")
public class Parcelado implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "titulo")
	private String titulo;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "data_realizacao")
	private LocalDate data;
	
	@Column(name = "data_registro")
	private LocalDateTime dataRegistro;
	
	@Column(name = "quitado")
	private Boolean quitado;
	
	@OneToMany(mappedBy = "parcelado", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Parcela> parcelas = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_categoria")
	private Categoria categoria;
	
	public Parcelado() {
		
	}

	public Parcelado(Long id, String titulo, String descricao, LocalDate data, LocalDateTime dataRegistro,
			Boolean quitado) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.data = data;
		this.dataRegistro = dataRegistro;
		this.quitado = quitado;
	}
	
	public Parcelado(ParceladoSalvarDTO parcelaDTO) {
		this.titulo = parcelaDTO.getTitulo();
		this.descricao = parcelaDTO.getDescricao();
		this.data = parcelaDTO.getData();
		this.quitado = false;
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

	public Boolean getQuitado() {
		return quitado;
	}

	public void setQuitado(Boolean quitado) {
		this.quitado = quitado;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public List<Parcela> getParcelas() {
		return parcelas;
	}

	public void setParcelas(List<Parcela> parcelas) {
		this.parcelas = parcelas;
	}
	
	public void addParcela(Parcela parcela) {
		this.parcelas.add(parcela);
	}
	
	public void rmvParcela(Parcela parcela) {
		this.parcelas.remove(parcela);
	}

}
