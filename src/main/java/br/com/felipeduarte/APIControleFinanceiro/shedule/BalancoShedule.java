package br.com.felipeduarte.APIControleFinanceiro.shedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.felipeduarte.APIControleFinanceiro.service.BalancoService;
import br.com.felipeduarte.APIControleFinanceiro.service.CategoriaService;

@Component
@EnableScheduling
public class BalancoShedule {
	
	private static final String TIME_ZONE = "America/Sao_Paulo";
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private BalancoService balancoService;
	
	@Scheduled(cron = "0 0 0 1 * *",zone = TIME_ZONE)
	public void fechaBalanco() {
		System.out.println("Fechando os balanços do mês de todas as categorias de todos os usuários");
		this.balancoService.fecharBalancos();
	}
	
	@Scheduled(cron = "0 0 0 1 * *",zone = TIME_ZONE)
	public void abreBalanco() {
		System.out.println("Abrindo o balanço do mês para todas as categorias de todos os usuários");
		this.categoriaService.abrirBalanco();
	}
	
}
