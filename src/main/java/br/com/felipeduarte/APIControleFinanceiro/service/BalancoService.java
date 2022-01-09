package br.com.felipeduarte.APIControleFinanceiro.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.BalancoResumoDTO;
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
	
	public List<BalancoResumoDTO> buscarTodosResumo(Long idCategoria, Integer ano, Integer mes, 
			Integer qtdMes){
		
		//Só aceita número Impar
		if(qtdMes % 2 == 0) return null;
		
		Categoria categoria = this.categoriaService.buscarPorId(idCategoria);
		
		if(categoria == null) {
			return null;
		}
		
		//Verifica se categoria do balanço pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(categoria);
		
		Balanco balancoAtual = this.repository.findByCategoriaAndMesAndAno(categoria, mes, ano);
		if(balancoAtual == null) return null;
		
		List<Balanco> balancos = new ArrayList<>();
		
		LocalDate agora = LocalDate.now();
		
		if(LocalDate.of(balancoAtual.getAno(), balancoAtual.getMes(),1).isEqual(
				LocalDate.of(agora.getYear(), agora.getMonth(), 1))) { //Mês atual não tem meses depois
			
			
			for(int i=qtdMes-1; i > 0 ; i--) {
				
				LocalDate dataVez = agora.minusMonths(i);
				
				Balanco balancoVez = 
						this.repository.findByCategoriaAndMesAndAno(
								categoria, dataVez.getMonth().getValue(), dataVez.getYear());
				
				if(balancoVez != null) balancos.add(balancoVez);
				
			}
			
			balancos.add(balancoAtual);
			
		}else { //Mês não atual, existe meses pra frente e para atras
			
			LocalDate dataBalanco = LocalDate.of(ano, mes, 1);
			
			int metade = (qtdMes + 1) / 2 ;
			
			for(int i=metade-1; i > 0; i--) {
				
				LocalDate dataVez = dataBalanco.minusMonths(i);
				
				Balanco balancoVez = 
						this.repository.findByCategoriaAndMesAndAno(
								categoria, dataVez.getMonth().getValue(), dataVez.getYear());
				
				if(balancoVez != null) balancos.add(balancoVez);
				
			}
			
			balancos.add(balancoAtual);
			
			for(int i=metade+1; i <= qtdMes; i++) {
				
				LocalDate dataVez = dataBalanco.plusMonths(i);
				
				Balanco balancoVez = 
						this.repository.findByCategoriaAndMesAndAno(
								categoria, dataVez.getMonth().getValue(), dataVez.getYear());
				
				System.out.println("Data: " + dataVez);
				
				if(balancoVez != null) balancos.add(balancoVez);
				
			}
			
			
		}
		
		List<BalancoResumoDTO> balancosDTO = new ArrayList<>();
		
		for(Balanco b: balancos) {
			BalancoResumoDTO bdto = BalancoResumoDTO.converteBalancoParaBalancoResumoDTO(b);
			
			if((bdto.getAno().equals(ano)) && (bdto.getMes().equals(mes)))
				bdto.setAtual(true);
			else
				bdto.setAtual(false);
			
			balancosDTO.add(bdto);
		}
		
		return balancosDTO;
	}
	
	public Balanco recuperarAtual(Long idCategoria) {
		
		Categoria categoria = this.categoriaService.buscarPorId(idCategoria);
		
		if(categoria == null) {
			return null;
		}
		
		//Verifica se categoria do balanço pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(categoria);
		
		Balanco balanco = this.repository.findByCategoriaAndFechado(categoria, false);
		
		LocalDate agora = LocalDate.now(ZoneId.of(TIME_ZONE));
		
		//Verifica se mudou de mês - se sim solicita o cadastro do novo mês e fecha o anterior
		if(agora.getMonthValue() != balanco.getMes()) {
			
			balanco.setFechado(true);
			this.repository.save(balanco);
			
			Balanco novo = this.cadastrar(categoria);
			return novo;
		}
		
		return balanco;
	}
	
	public Balanco cadastrar(Categoria categoria) {
		
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
	
	public void atualizarSaldo(Balanco balanco) {
		
		Balanco b = this.buscarPorId(balanco.getId());
		
		if(b != null) {
			
			double saldoAnterior = balanco.getSaldoAnterior();
			double proventos = 0.00;
			double despesas = 0.00;
			
			for(Lancamento l: b.getLancamentos()) {
				
				if(l.getTipo().getValor() == TipoLancamentoEnum.PROVENTO.getValor()) {
					proventos += l.getValor();
				}
				
				if(l.getTipo().getValor() == TipoLancamentoEnum.DESPESA.getValor()) {
					despesas += l.getValor();
				}
				
			}
			
			Double saldo = saldoAnterior + proventos - despesas;
			
			b.setSaldoAtual(Precision.round(saldo, 2)); 
			
			this.repository.save(b);
			
		}
		
	}
	
}
