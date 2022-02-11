package br.com.felipeduarte.APIControleFinanceiro.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.model.enums.TipoUsuario;

public class UsuarioDTO {
	
	private Long id;
	private String nome;
	private String email;
	private Set<String> tipo;
	
	public UsuarioDTO(Usuario usuario) {
		this.id = usuario.getId();
		this.nome = usuario.getNome();
		this.email = usuario.getEmail();
		this.tipo = usuario.getTipo().stream().map(t -> TipoUsuario.toEnum(t).getDescricao())
				.collect(Collectors.toSet());
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

	public Set<String> getTipo() {
		return tipo;
	}

	public void setTipo(Set<String> tipo) {
		this.tipo = tipo;
	}

}
