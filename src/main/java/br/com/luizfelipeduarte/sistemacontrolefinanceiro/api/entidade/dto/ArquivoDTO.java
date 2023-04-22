package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import org.springframework.core.io.InputStreamResource;

public class ArquivoDTO {

	private String nomeArquivo;
	
	private InputStreamResource arquivo;
	
	public ArquivoDTO() {
		
	}
	
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