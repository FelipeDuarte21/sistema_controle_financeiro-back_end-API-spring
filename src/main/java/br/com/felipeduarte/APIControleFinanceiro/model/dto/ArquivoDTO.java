package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import org.springframework.core.io.InputStreamResource;

import io.swagger.annotations.ApiModelProperty;

public class ArquivoDTO {
	
	@ApiModelProperty(value = "Nome do arquivo")
	private String nomeArquivo;
	
	@ApiModelProperty(value = "Arquivo")
	private InputStreamResource arquivo;
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	public InputStreamResource getArquivo() {
		return arquivo;
	}
	
	public void setArquivo(InputStreamResource arquivo) {
		this.arquivo = arquivo;
	}
	
}