package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Parcelado;

public class ParceladoDTO {

	private Long id;
	
	private String titulo;

	private String descricao;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate data;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime dataRegistro;
	
	private BigDecimal valorTotal;
	
	private BigDecimal valorPago;
	
	private Integer totalParcelas;
	
	private Integer totalParcelasPagas;
	
	private Boolean quitado;
	
	private List<ParcelaDTO> parcelas = new ArrayList<>();
	
	public ParceladoDTO() {
		
	}

	public ParceladoDTO(Parcelado parcelado) {
		this.id = parcelado.getId();
		this.titulo = parcelado.getTitulo();
		this.descricao = parcelado.getDescricao();
		this.data = parcelado.getData();
		this.dataRegistro = parcelado.getDataRegistro();
		this.valorTotal = parcelado.getValorTotal();
		this.valorPago = parcelado.getValorPago();
		this.totalParcelas = parcelado.getTotalParcelas();
		this.totalParcelasPagas = parcelado.getTotalParcelasPagas();
		this.quitado = parcelado.getQuitado();
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

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public Integer getTotalParcelas() {
		return totalParcelas;
	}

	public void setTotalParcelas(Integer totalParcelas) {
		this.totalParcelas = totalParcelas;
	}

	public Integer getTotalParcelasPagas() {
		return totalParcelasPagas;
	}

	public void setTotalParcelasPagas(Integer totalParcelasPagas) {
		this.totalParcelasPagas = totalParcelasPagas;
	}

	public Boolean getQuitado() {
		return quitado;
	}

	public void setQuitado(Boolean quitado) {
		this.quitado = quitado;
	}

	public List<ParcelaDTO> getParcelas() {
		return parcelas;
	}

	public void setParcelas(List<ParcelaDTO> parcelas) {
		this.parcelas = parcelas;
	}

}
