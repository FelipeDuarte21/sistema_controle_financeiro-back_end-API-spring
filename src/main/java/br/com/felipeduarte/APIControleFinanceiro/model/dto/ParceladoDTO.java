package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.felipeduarte.APIControleFinanceiro.model.Parcelado;
import io.swagger.annotations.ApiModelProperty;

public class ParceladoDTO {
	
	@ApiModelProperty(value = "Identificação do parcelado")
	private Long id;
	
	@ApiModelProperty(value = "titulo do parcelado")
	private String titulo;
	
	@ApiModelProperty(value = "descrição do parcelado")
	private String descricao;
	
	@ApiModelProperty(value = "data de realização do parcelado")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	
	@ApiModelProperty(value = "data que foi registrado o parcelado")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime dataRegistro;
	
	@ApiModelProperty(value = "Parcelado pago")
	private Boolean quitado;
	
	@ApiModelProperty(value = "Categoria a qual pertence o parcelado")
	private CategoriaDTO categoria;
	
	@ApiModelProperty(value = "parcelas do parcelado")
	private List<ParcelaDTO> parcelas = new ArrayList<>();
	
	public ParceladoDTO() {
		
	}

	public ParceladoDTO(Long id, String titulo, String descricao, LocalDate data, LocalDateTime dataRegistro,
			Boolean quitado, CategoriaDTO categoria, List<ParcelaDTO> parcelas) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.data = data;
		this.dataRegistro = dataRegistro;
		this.quitado = quitado;
		this.categoria = categoria;
		this.parcelas = parcelas;
	}
	
	public ParceladoDTO(Parcelado parcelado) {
		this.id = parcelado.getId();
		this.titulo = parcelado.getTitulo();
		this.descricao = parcelado.getDescricao();
		this.data = parcelado.getData();
		this.dataRegistro = parcelado.getDataRegistro();
		this.quitado = parcelado.getQuitado();
		this.categoria = new CategoriaDTO(parcelado.getCategoria());
		this.parcelas = parcelado.getParcelas().stream().map(ParcelaDTO::new).collect(Collectors.toList());
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

	public CategoriaDTO getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaDTO categoria) {
		this.categoria = categoria;
	}

	public List<ParcelaDTO> getParcelas() {
		return parcelas;
	}

	public void setParcelas(List<ParcelaDTO> parcelas) {
		this.parcelas = parcelas;
	}

}
