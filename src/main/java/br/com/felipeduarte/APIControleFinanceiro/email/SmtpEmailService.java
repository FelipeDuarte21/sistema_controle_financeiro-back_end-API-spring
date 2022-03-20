package br.com.felipeduarte.APIControleFinanceiro.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstratoEmailService{
	
	@Autowired
	private MailSender mailSender;

	@Override
	public void enviarEmail(SimpleMailMessage msg) {
		mailSender.send(msg);
	}

}
