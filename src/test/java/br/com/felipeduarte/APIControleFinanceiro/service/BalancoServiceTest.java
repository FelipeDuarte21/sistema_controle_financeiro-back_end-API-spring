package br.com.felipeduarte.APIControleFinanceiro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.TipoLancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.BalancoDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.enums.TipoLancamentoEnum;
import br.com.felipeduarte.APIControleFinanceiro.repository.BalancoRepository;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

class BalancoServiceTest {

	private BalancoService balancoService;
	
	@Mock
	private Clock clock;
	
	@Mock
	private BalancoRepository balancoRepository;
	
	@Mock
	private CategoriaService categoriaService;
	
	@Captor
	private ArgumentCaptor<Balanco> captor; 
	
	@BeforeEach
	private void inicializar() {
		MockitoAnnotations.openMocks(this);
		this.balancoService = new BalancoService(clock, balancoRepository, categoriaService);
	}
	
	@Test
	void buscarFaixaDeBalancosObterErroDeBalancoNaoEncontrado() {
		
		Mockito.when(balancoRepository.findByCategoriaAndMesAno(Mockito.any(), Mockito.any()))
			.thenReturn(Optional.empty());
		
		assertThrows(IllegalParameterException.class, 
				() -> this.balancoService.buscarFaixas(1L, 2022, 1, 5));
		
	}
	
	
	@Test
	void buscarFaixaDeBalancosObterErroDeMesInvalido() {
		
		assertThrows(IllegalParameterException.class, 
				() -> this.balancoService.buscarFaixas(1L, 2022, 13, 5));
		
	}
	
	@Test
	void buscarFaixaDeBalancosObterErroDeQuantidadePar() {
		
		assertThrows(IllegalParameterException.class, 
				() -> this.balancoService.buscarFaixas(1L, 2022, 1, 6));
		
	}
	
	@Test
	void recuperarBalancoAtualObterBalancoNovo() {
		
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		Mockito.when(categoriaService.buscarPorIdInterno(Mockito.anyLong())).thenReturn(new Categoria());
		
		Mockito.when(balancoRepository.findByCategoriaAndMesAno(Mockito.any(), Mockito.any()))
			.thenReturn(Optional.empty());
		
		this.balancoService.recuperarAtualInterno(1L);
		
		Mockito.verify(balancoRepository).save(captor.capture());
		
		Balanco balancoNovo = captor.getValue();
		
		assertEquals(YearMonth.now(), balancoNovo.getMesAno());
		assertEquals(new BigDecimal("0"), balancoNovo.getSaldoAtual());
		assertFalse(balancoNovo.getFechado());
		
	}
	
	@Test
	void recuperarBalancoAtualObterBalancoEncontrado() {
		
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		Mockito.when(categoriaService.buscarPorIdInterno(Mockito.anyLong())).thenReturn(new Categoria());
		
		Balanco balanco = getBalancos().get(0);
		
		Mockito.when(balancoRepository.findByCategoriaAndMesAno(Mockito.any(), Mockito.any()))
			.thenReturn(Optional.of(balanco));
		
		Balanco balancoRetornado = this.balancoService.recuperarAtualInterno(1L);
		
		assertEquals(balanco.getId(), balancoRetornado.getId());
		assertEquals(balanco.getSaldoAtual(), balancoRetornado.getSaldoAtual());
		assertEquals(balanco.getFechado(), balancoRetornado.getFechado());
		
	}
	
	@Test
	void cadastrarBalancoObterBalancoCadastrado() {
		
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		Categoria categoria = new Categoria(1L,"Teste","Testando...",LocalDate.now());
		
		Balanco balancoAnterior = 
				new Balanco(1L,YearMonth.now().minusMonths(1L),new BigDecimal("100"),
						new BigDecimal("200"),true);
		
		Mockito.when(balancoRepository.findByCategoriaAndMesAno(Mockito.any(), Mockito.any()))
			.thenReturn(Optional.of(balancoAnterior));
		
		this.balancoService.cadastrar(categoria);
		
		Mockito.verify(balancoRepository).save(captor.capture());
		
		Balanco balancoRetornado = captor.getValue();
		
		assertEquals(YearMonth.now(), balancoRetornado.getMesAno());
		assertEquals(new BigDecimal("200"), balancoRetornado.getSaldoAtual());
		assertFalse(balancoRetornado.getFechado());
		
	}
	
	@Test
	void bucarPorDataObterBalancoVazio() {
			
		Mockito.when(balancoRepository.findByCategoriaAndMesAno(Mockito.any(), Mockito.any()))
			.thenReturn(Optional.empty());
		
		BalancoDTO balancoRetornado = this.balancoService.buscarPorData(1L, 2, 2022);
		
		assertNull(balancoRetornado.getId());
		assertNull(balancoRetornado.getSaldoAnterior());
		assertNull(balancoRetornado.getSaldoAtual());
		
	}
	
	@Test
	void buscarPorDataObterErroDeMesInvalido() {
		
		assertThrows(IllegalParameterException.class, () -> 
			this.balancoService.buscarPorData(1L, 13, 2022));
		
	}
	
	@Test
	void buscarPorDataObterBalanco() {
		
		Categoria categoria = new Categoria(1L,"Teste","Testando...",LocalDate.now());
		
		Mockito.when(categoriaService.buscarPorIdInterno(Mockito.anyLong()))
			.thenReturn(categoria);
		
		Balanco balanco = getBalancos().get(0);
		balanco.setCategoria(categoria);
		
		Mockito.when(balancoRepository.findByCategoriaAndMesAno(Mockito.any(), Mockito.any()))
			.thenReturn(Optional.of(balanco));
		
		BalancoDTO balancoRetornado = this.balancoService.buscarPorData(1L, 2, 2022);
		
		assertEquals(balanco.getId(), balancoRetornado.getId());
		assertEquals(balanco.getSaldoAtual(), balancoRetornado.getSaldoAtual());
		
	}
	
	@Test
	void buscarPorIdObterErroDeBalancoNaoEncontrado() {
		
		Mockito.when(balancoRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		assertThrows(ObjectNotFoundFromParameterException.class, () -> this.balancoService.buscarPorId(1L));
		
	}
	
	@Test
	void buscarPorIdObterOBalanco() {
		
		Balanco balanco = getBalancos().get(0);
		
		Mockito.when(balancoRepository.findById(Mockito.anyLong()))
		.thenReturn(Optional.of(balanco));
		
		Balanco balancoRetornado = this.balancoService.buscarPorId(1L);
		
		assertEquals(balanco.getId(), balancoRetornado.getId());
		assertEquals(balanco.getSaldoAtual(), balancoRetornado.getSaldoAtual());
		
	}

	@Test
	void atualizarSaldoRetornarSaldoCalculadoCorretamente() {
		
		Balanco balanco = getBalancos().get(0);
		
		TipoLancamento tipoProvento = new TipoLancamento(1L,TipoLancamentoEnum.PROVENTO.getValor(),
				TipoLancamentoEnum.PROVENTO.getNome());
		
		TipoLancamento tipoDespesa = new TipoLancamento(2L,TipoLancamentoEnum.DESPESA.getValor(),
				TipoLancamentoEnum.DESPESA.getNome());
		
		balanco.addLancamento(new Lancamento(1L,"Lancamento1", "Lancamento de Teste",new BigDecimal("100"),
				LocalDate.now(),LocalDateTime.now(),tipoProvento));
		
		balanco.addLancamento(new Lancamento(2L,"Lancamento2", "Lancamento de Teste",new BigDecimal("50"),
				LocalDate.now(),LocalDateTime.now(),tipoProvento));
		
		balanco.addLancamento(new Lancamento(3L,"Lancamento3", "Lancamento de Teste",new BigDecimal("100"),
				LocalDate.now(),LocalDateTime.now(),tipoDespesa));
		
		this.balancoService.atualizarSaldo(balanco);
		
		Mockito.verify(balancoRepository).save(captor.capture());
		
		Balanco balancoRecuperado = captor.getValue();
		
		assertEquals(new BigDecimal("150"), balancoRecuperado.getSaldoAtual());
		
	}
	
	public List<Balanco> getBalancos(){
		
		var balancos = new ArrayList<Balanco>();
		
		balancos.add(new Balanco(1L,YearMonth.now(),new BigDecimal("100"),new BigDecimal("0"),false));
		balancos.add(new Balanco(2L,YearMonth.now().minusMonths(1),new BigDecimal("100"),new BigDecimal("0"),false));
		balancos.add(new Balanco(3L,YearMonth.now().minusMonths(2),new BigDecimal("100"),new BigDecimal("0"),false));
		balancos.add(new Balanco(4L,YearMonth.now().minusMonths(3),new BigDecimal("100"),new BigDecimal("0"),false));
		
		return balancos;
		
	}
	
}
