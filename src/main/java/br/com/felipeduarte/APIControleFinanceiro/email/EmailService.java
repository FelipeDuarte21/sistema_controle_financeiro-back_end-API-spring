package br.com.felipeduarte.APIControleFinanceiro.email;

import org.springframework.mail.SimpleMailMessage;

import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;

public interface EmailService {
	
	void enviarEmailNovaSenha(Usuario usuario, String senha);
	
	void enviarEmail(SimpleMailMessage msg);

}