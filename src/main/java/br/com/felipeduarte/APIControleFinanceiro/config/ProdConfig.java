package br.com.felipeduarte.APIControleFinanceiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.felipeduarte.APIControleFinanceiro.email.EmailService;
import br.com.felipeduarte.APIControleFinanceiro.email.SmtpEmailService;

@Configuration
@Profile("prod")
public class ProdConfig {
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
	
}
