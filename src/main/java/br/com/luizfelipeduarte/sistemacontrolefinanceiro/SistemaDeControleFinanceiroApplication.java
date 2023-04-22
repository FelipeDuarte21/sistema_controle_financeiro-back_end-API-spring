package br.com.luizfelipeduarte.sistemacontrolefinanceiro;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SistemaDeControleFinanceiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeControleFinanceiroApplication.class, args);
	}
	
	@Bean
	public Clock clock() {
		return Clock.system(ZoneId.of("America/Sao_Paulo"));
	}

}
