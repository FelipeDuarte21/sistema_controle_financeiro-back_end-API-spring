package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Usuario;

public class UsuarioDTO {
	
	private Long id;
	
	private String nome;
	
	private String email;
	
	private ContaDTO conta;
	
	public UsuarioDTO(Usuario usuario) {
		this.id = usuario.getId();
		this.nome = usuario.getNome();
		this.email = usuario.getEmail();
		this.conta = new ContaDTO(usuario.getConta());
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ContaDTO getConta() {
		return conta;
	}

	public void setConta(ContaDTO conta) {
		this.conta = conta;
	}

}
