package br.com.felipeduarte.APIControleFinanceiro.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.enums.TipoLancamentoEnum;
import br.com.felipeduarte.APIControleFinanceiro.repository.BalancoRepository;

@Service
public class BalancoService {
	
	private static final String TIME_ZONE = "America/Sao_Paulo";
	
	@Autowired
	private BalancoRepository repository;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private RestricaoService restricaoService;
	
	public Balanco recuperarAtual(Long idCategoria) {
		
		Categoria categoria = this.categoriaService.buscarPorId(idCategoria);
		
		if(categoria == null) {
			return null;
		}
		
		//Verifica se categoria do balanço pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(categoria);
		
		Balanco balanco = this.repository.findByCategoriaAndFechado(categoria, false);
		
		return balanco;
	}
	
	public Balanco recuperarPorData(Long idCategoria, Integer mes, Integer ano) {
		
		Categoria categoria = this.categoriaService.buscarPorId(idCategoria);
		
		if(categoria == null) {
			return null;
		}
		
		//Verifica se categoria do balanço pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(categoria);
		
		Balanco balanco = this.repository.findByCategoriaAndMesAndAno(categoria, mes, ano);
		
		return balanco;
	}
	
	public Balanco buscarPorId(Long id) {
		
		Optional<Balanco> balanco = this.repository.findById(id);
		
		if(balanco.isEmpty()) {
			return null;
		}
		
		return balanco.get();
	}
	
	public void cadastrar(Categoria categoria) {
		
		LocalDate agora = LocalDate.now(ZoneId.of(TIME_ZONE));
		
		LocalDate dataAnterior = agora.minusMonths(1);
		Balanco balancoAnterior = this.repository.findByCategoriaAndMesAndAno(categoria, 
				dataAnterior.getMonthValue(), dataAnterior.getYear());
		double saldoAnterior = 0.0;
		if(balancoAnterior != null) {
			saldoAnterior = balancoAnterior.getSaldoAtual();
		}
		
		Balanco balanco = new Balanco();
		balanco.setId(null);
		balanco.setMes(agora.getMonthValue());
		balanco.setAno(agora.getYear());
		balanco.setSaldoAnterior(saldoAnterior);
		balanco.setSaldoAtual(saldoAnterior);
		balanco.setFechado(false);
		balanco.setCategoria(categoria);
		
		balanco = this.repository.save(balanco);
		
	}
	
	public void atualizarSaldo(Balanco balanco) {
		
		Balanco b = this.buscarPorId(balanco.getId());
		
		if(b != null) {
			
			double saldoAnterior = balanco.getSaldoAnterior();
			double proventos = 0.0;
			double despesas = 0.0;
			
			for(Lancamento l: b.getLancamentos()) {
				
				if(l.getTipo().getValor() == TipoLancamentoEnum.PROVENTO.getValor()) {
					proventos += l.getValor();
				}
				
				if(l.getTipo().getValor() == TipoLancamentoEnum.DESPESA.getValor()) {
					despesas += l.getValor();
				}
				
			}
			
			b.setSaldoAtual(saldoAnterior + proventos - despesas); 
			
			this.repository.save(b);
			
		}
		
	}
	
	public void fecharBalancos() {
		
		LocalDate hoje = LocalDate.now(ZoneId.of(TIME_ZONE));
		LocalDate mesPassado = hoje.minusMonths(1);
		
		List<Balanco> balancos = this.repository.findByFechadoAndMesAndAno(false,
				mesPassado.getMonthValue(),mesPassado.getYear());;
		
		if(balancos != null) {
			
			balancos.forEach(balanco -> {
				balanco.setFechado(true);
				this.repository.save(balanco);
			});
			
		}
		
	}
	
}
