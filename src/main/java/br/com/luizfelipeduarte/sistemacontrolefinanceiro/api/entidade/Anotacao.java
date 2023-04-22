package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade;

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

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.AnotacaoDadosDTO;

@Entity
@Table(name = "anotacao")
public class Anotacao {

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
	
	public Anotacao() {
		this.riscado = false;
	}
	
	public Anotacao(AnotacaoDadosDTO anotacaoDadosDTO) {
		this.titulo = anotacaoDadosDTO.getTitulo();
		this.descricao = anotacaoDadosDTO.getDescricao();
		this.data = anotacaoDadosDTO.getData();
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
		Anotacao other = (Anotacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
