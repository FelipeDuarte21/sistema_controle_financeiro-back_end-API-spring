package br.com.felipeduarte.APIControleFinanceiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.felipeduarte.APIControleFinanceiro.email.EmailService;
import br.com.felipeduarte.APIControleFinanceiro.email.MockEmailService;

@Configuration
@Profile("dsv")
public class DsvConfig {

	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
	
}