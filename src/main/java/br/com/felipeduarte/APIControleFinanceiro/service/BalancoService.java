package br.com.felipeduarte.APIControleFinanceiro.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.BalancoDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.BalancoFaixaDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.enums.TipoLancamentoEnum;
import br.com.felipeduarte.APIControleFinanceiro.repository.BalancoRepository;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

@Service
public class BalancoService {
	
	private Clock clock;
	
	private BalancoRepository repository;
	
	private CategoriaService categoriaService;
	
	@Autowired
	public BalancoService(Clock clock, BalancoRepository repository, CategoriaService categoriaService) {
		this.clock = clock;
		this.repository = repository;
		this.categoriaService = categoriaService;
	}

	public List<BalancoFaixaDTO> buscarFaixas(Long idCategoria, Integer ano, Integer mes, 
			Integer qtdMes){
		
		//Só aceita a quantidade de mes em número Impar
		if(qtdMes % 2 == 0) throw new IllegalParameterException("Erro! quantide de mês não pode ser par!");
		
		try {
			
			var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
			
			var anoMes = YearMonth.of(ano, Month.of(mes));
			
			var optBalancoFoco = this.repository.findByCategoriaAndMesAno(categoria, anoMes);
			
			if(!optBalancoFoco.isPresent()) 
				throw new IllegalParameterException(
						"Erro! balanco não encontrado para ano e mes informado!");
			
			var balancos = new ArrayList<>();
			
			var anoMesAgora = YearMonth.now(clock.getZone());
			
			//O mês e ano recebido é o mês e ano atual então só tem balanços anterior a data atual
			if(anoMes.equals(anoMesAgora)){
				
				var balancosIntervalo = 
						this.repository.findByCategoriaAndMesAnoBetween(categoria,
								anoMesAgora.minusMonths(qtdMes-1), anoMesAgora);
				
				balancos.addAll(balancosIntervalo);
				
			}else {				
				//O mês e ano recebido não é o mês e ano atual então tem balanços anteriores e posteriores
				
				var metade = (qtdMes + 1 ) / 2;
				
				var balancosIntervaloAnterior = 
						this.repository.findByCategoriaAndMesAnoBetween(categoria,
								anoMes.minusMonths(metade-1), anoMes);
				
				var balancosIntervalorPosterior = 
						this.repository.findByCategoriaAndMesAnoBetween(categoria,
								anoMes.plusMonths(1L), anoMes.plusMonths(metade-1));
				
				balancos.addAll(balancosIntervaloAnterior);
				balancos.addAll(balancosIntervalorPosterior);
			
			}
			
			return balancos.stream().map(balanco -> {
				var balancoDTO = new BalancoFaixaDTO((Balanco) balanco);
				if(balancoDTO.getMesAno().equals(anoMesAgora)) balancoDTO.setAtual(true);
				return balancoDTO;
			}).collect(Collectors.toList());
			
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new IllegalParameterException(ex.getMessage());
			
		}catch(DateTimeException ex) {
			throw new IllegalParameterException("Erro! mês informado é inválido!");
			
		}
	
	}
	
	public BalancoDTO recuperarAtual(Long idCategoria) {
		
		var balanco = recuperarAtualInterno(idCategoria);
		
		return new BalancoDTO(balanco);
	
	}
	
	public Balanco recuperarAtualInterno(Long idCategoria) {
		
		try {
			
			var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
			
			var mesAnoAgora = YearMonth.now(clock.getZone());
			
			var optBalanco = this.repository.findByCategoriaAndMesAno(categoria, mesAnoAgora);
			
			if(optBalanco.isPresent()) return optBalanco.get();
			
			var balancoNovo = cadastrar(categoria);
			
			optBalanco.get().setFechado(true);
			
			this.repository.save(optBalanco.get());
			
			return balancoNovo;
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new IllegalParameterException(ex.getMessage());
			
		}
	
	}
	
	public Balanco cadastrar(Categoria categoria) {
		
		var anoMesAgora = YearMonth.now(clock.getZone());
		
		var anoMesPassado = anoMesAgora.minusMonths(1L);
		
		var optBalancoMesPassado = 
				this.repository.findByCategoriaAndMesAno(categoria, anoMesPassado);
		
		var saldoAnterior = new BigDecimal("0");
		
		if(optBalancoMesPassado.isPresent()) saldoAnterior.add(optBalancoMesPassado.get().getSaldoAtual());
		
		var balanco = new Balanco(null,anoMesAgora,saldoAnterior,saldoAnterior,false);
		balanco.setCategoria(categoria);
		
		balanco = this.repository.save(balanco);
		
		return balanco;
		
	}
	
	public BalancoDTO buscarPorData(Long idCategoria, Integer mes, Integer ano) {
		
		try {
			
			var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
			
			var mesAno = YearMonth.of(ano, Month.of(mes));
			
			var optBalanco = this.repository.findByCategoriaAndMesAno(categoria, mesAno);
			
			if(optBalanco.isEmpty()) return new BalancoDTO();
			
			return new BalancoDTO(optBalanco.get());
			
		}catch(DateTimeException ex) {
			throw new IllegalParameterException("Erro! mês informado é inválido!");
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new IllegalParameterException(ex.getMessage());
			
		}
		
	}
	
	public Balanco buscarPorId(Long id) {
		
		var optBalanco = this.repository.findById(id);
		
		if(!optBalanco.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! balanco não encontrado para o id informado!");
		
		return optBalanco.get();
	}
	
	public void atualizarSaldo(Balanco balanco) {
			
		var proventos = new BigDecimal("0");
		var despesas = new BigDecimal("0");
		
		balanco.getLancamentos().forEach(lancamento -> {
			
			if(lancamento.getTipo().getValor().equals(TipoLancamentoEnum.PROVENTO.getValor()))
				proventos.add(lancamento.getValor());
				
			if(lancamento.getTipo().getValor().equals(TipoLancamentoEnum.DESPESA.getValor()))
				despesas.add(lancamento.getValor());
			
		});
		
		var saldo = new BigDecimal("0").add(balanco.getSaldoAnterior()).add(proventos).subtract(despesas);
		
		balanco.setSaldoAtual(saldo);
		
		this.repository.save(balanco);
		
	}
	
	public void atulizarBalanco(Balanco balanco) {
		this.repository.save(balanco);
	}
	
}
