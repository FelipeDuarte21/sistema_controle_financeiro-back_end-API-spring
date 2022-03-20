package br.com.felipeduarte.APIControleFinanceiro.email;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;

public abstract class AbstratoEmailService implements EmailService{
	
	@Value("${default.sender}")
	private String emailFrom;

	@Override
	public void enviarEmailNovaSenha(Usuario usuario, String senha) {
		SimpleMailMessage msg = preparaMensagemDeNovaSenha(usuario,senha);
		enviarEmail(msg);
	}
	
	protected SimpleMailMessage preparaMensagemDeNovaSenha(Usuario usuario, String senha) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(usuario.getEmail());
		sm.setFrom(emailFrom);
		sm.setSubject("Sistema de Controle Financeiro - Nova Senha");
		sm.setSentDate(new Date());
		sm.setText("Nova senha gerada: "+ senha);
		return sm;
	}

}
