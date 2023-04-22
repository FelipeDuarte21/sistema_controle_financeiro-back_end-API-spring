package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

public class TokenDTO {
	
	private String token;
	private String tipo;
	private Long idUsuario;
	
	public TokenDTO(String token, String tipo, Long idUsuario) {
		this.token = token;
		this.tipo = tipo;
		this.idUsuario = idUsuario;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

}
