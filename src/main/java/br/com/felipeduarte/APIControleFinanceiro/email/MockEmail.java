package br.com.felipeduarte.APIControleFinanceiro.email;

import org.springframework.mail.SimpleMailMessage;

public class MockEmail extends AbstratoEmailService {

	@Override
	public void enviarEmail(SimpleMailMessage msg) {
		System.out.println("Enviando email");
		System.out.println("Mensagem: " + msg);
		System.out.println("Email enviado!");
	}

}
