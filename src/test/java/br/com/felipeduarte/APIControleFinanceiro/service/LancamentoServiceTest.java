package br.com.felipeduarte.APIControleFinanceiro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.TipoLancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.enums.TipoLancamentoEnum;
import br.com.felipeduarte.APIControleFinanceiro.repository.LancamentoRepository;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

class LancamentoServiceTest {
	
	private LancamentoService service;
	
	@Mock
	private Clock clock;
	
	@Mock
	private LancamentoRepository repository;
	
	@Mock
	private BalancoService balancoService;
	
	@Mock
	private TipoLancamentoService tipoLancamentoService;
	
	@Mock
	private RestricaoService restricaoService;
	
	@Captor
	private ArgumentCaptor<Lancamento> captor;
	

	@BeforeEach
	private void inicializar() {
		MockitoAnnotations.openMocks(this);
		this.service = new LancamentoService(clock,repository,balancoService,tipoLancamentoService,
				restricaoService);
	}
	
	@Test
	void salvarLancamentoObterErroDeBalancoFechado() {
		
		Categoria categoria = new Categoria();
		
		Balanco balanco = new Balanco();
		balanco.setCategoria(categoria);
		balanco.setFechado(false);
		
		Mockito.when(balancoService.buscarPorId(Mockito.anyLong())).thenReturn(balanco);
		
		LancamentoSalvarDTO lancamentoDTO = new LancamentoSalvarDTO("Salvando","lorem ipsum is dolor",
				new BigDecimal("100"),LocalDate.now(),1);
		
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		Lancamento lancamentoPosSalvo = new Lancamento(lancamentoDTO);
		lancamentoPosSalvo.setBalanco(balanco);
		lancamentoPosSalvo.setTipo(new TipoLancamento());
		
		Mockito.when(repository.save(Mockito.any())).thenReturn(lancamentoPosSalvo);
		
		this.service.salvar(1L, lancamentoDTO);
		
		LancamentoDTO lancamentoSalvo = this.service.salvar(1L, lancamentoDTO);
		
		assertEquals(lancamentoDTO.getNome(), lancamentoSalvo.getNome());
		assertEquals(lancamentoDTO.getDescricao(), lancamentoSalvo.getDescricao());
		assertEquals(lancamentoDTO.getData(), lancamentoSalvo.getData());
		
	}
	
	@Test
	void salvarLancamentoConseguirSalvar() {
		
		Categoria categoria = new Categoria();
		
		Balanco balanco = new Balanco();
		balanco.setCategoria(categoria);
		balanco.setFechado(true);
		
		Mockito.when(balancoService.buscarPorId(Mockito.anyLong())).thenReturn(balanco);
		
		LancamentoSalvarDTO lancamentoDTO = new LancamentoSalvarDTO("Salvando","lorem ipsum is dolor",
				new BigDecimal("100"),LocalDate.now(),1);
		
		assertThrows(IllegalParameterException.class, () -> this.service.salvar(1L, lancamentoDTO));
		
	}
	
	@Test
	void atualizarLancamentoObterErroDeBalancoFechado() {
		
		Categoria categoria = new Categoria();
		
		Balanco balanco = new Balanco();
		balanco.setCategoria(categoria);
		balanco.setFechado(true);
		
		Lancamento lancamento = getLancamentos().get(0);
		lancamento.setBalanco(balanco);
		
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(lancamento));
		
		LancamentoSalvarDTO lancamentoDTO = new LancamentoSalvarDTO("Atualizando Teste1","Teste blblbl",
				new BigDecimal("100"),LocalDate.now(),1);
		
		assertThrows(IllegalParameterException.class, () -> this.service.alterar(1L, lancamentoDTO));
		
	}
	
	@Test
	void atualizarLancamentoObterErroDeLancamentoNaoEncontrado() {
		
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		LancamentoSalvarDTO lancamentoDTO = new LancamentoSalvarDTO("Atualizando Teste1","Teste blblbl",
				new BigDecimal("100"),LocalDate.now(),1);
		
		assertThrows(ObjectNotFoundFromParameterException.class, 
				() -> this.service.alterar(1L, lancamentoDTO)) ;
		
	}
	
	@Test
	void atualizarLancamentoObterErroDeIdZero() {
		
		LancamentoSalvarDTO lancamentoDTO = new LancamentoSalvarDTO("Atualizando Teste1","Teste blblbl",
				new BigDecimal("100"),LocalDate.now(),1);
		
		assertThrows(IllegalParameterException.class, () -> this.service.alterar(0L, lancamentoDTO));
		
	}
	
	@Test
	void atualizarLancamentoObterErroDeIdNullo() {
		
		LancamentoSalvarDTO lancamentoDTO = new LancamentoSalvarDTO("Atualizando Teste1","Teste blblbl",
				new BigDecimal("100"),LocalDate.now(),1);
		
		assertThrows(IllegalParameterException.class, () -> this.service.alterar(null, lancamentoDTO));
		
	}
	
	@Test
	void atualizarLancamentoConseguirAtualizar() {
		
		Categoria categoria = new Categoria();
		
		Balanco balanco = new Balanco();
		balanco.setCategoria(categoria);
		balanco.setFechado(false);
		
		Lancamento lancamento = getLancamentos().get(0);
		lancamento.setBalanco(balanco);
		
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(lancamento));
		
		LancamentoSalvarDTO lancamentoDTO = new LancamentoSalvarDTO("Atualizando Teste1","Teste blblbl",
				new BigDecimal("100"),LocalDate.now(),1);
		
		Lancamento lancamentoPosSalvo = new Lancamento(1L,"Atualizando Teste1","Teste blblbl",
				new BigDecimal("100"),LocalDate.now(),LocalDateTime.now(), new TipoLancamento());
		lancamentoPosSalvo.setBalanco(balanco);
		
		Mockito.when(repository.save(Mockito.any())).thenReturn(lancamentoPosSalvo);
		
		LancamentoDTO lancamentoAtualizado = this.service.alterar(1L, lancamentoDTO);
		
		assertEquals(lancamentoDTO.getNome(), lancamentoAtualizado.getNome());
		assertEquals(lancamentoDTO.getDescricao(), lancamentoAtualizado.getDescricao());
		assertEquals(lancamentoDTO.getValor(), lancamentoAtualizado.getValor());
		assertEquals(lancamentoDTO.getData(), lancamentoAtualizado.getData());
		
	}
	
	@Test
	void excluirLancamentoObterErroDeBalancoFechado() {
		
		Categoria categoria = new Categoria();
		
		Balanco balanco = new Balanco();
		balanco.setCategoria(categoria);
		balanco.setFechado(true);
		
		Lancamento lancamento = getLancamentos().get(0);
		lancamento.setBalanco(balanco);
		
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(lancamento));
		
		assertThrows(IllegalParameterException.class, () -> this.service.excluir(1L) );
		
	}
	
	@Test
	void excluirLancamentoObterErroDeLancamentoNaoEncontrado() {
		
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		assertThrows(ObjectNotFoundFromParameterException.class, () -> this.service.excluir(1L));
		
	}
	
	@Test
	void excluirLancamentoConseguirExcluir() {
		
		Categoria categoria = new Categoria();
		
		Balanco balanco = new Balanco();
		balanco.setCategoria(categoria);
		balanco.setFechado(false);
		
		Lancamento lancamento = getLancamentos().get(0);
		lancamento.setBalanco(balanco);
		
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(lancamento));
		
		this.service.excluir(1L);
		
		Mockito.verify(repository).delete(captor.capture());
		
		Lancamento lancamentoRecuperado = captor.getValue();
		
		assertEquals(lancamento.getId(), lancamentoRecuperado.getId());
		
	}
	
	@Test
	void obterLancamentoPeloIdObterErroDeLancamentoNaoEncontrado() {
		
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		assertThrows(ObjectNotFoundFromParameterException.class, () -> this.service.buscarPorId(1L));
		
	}
	
	@Test
	void obterLancamentoPeloIdConseguirLancamento() {
		
		Categoria categoria = new Categoria();
		
		Balanco balanco = new Balanco();
		
		balanco.setCategoria(categoria);
		
		Lancamento lancamento = getLancamentos().get(0);
		
		lancamento.setBalanco(balanco);
		
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(lancamento));
		
		LancamentoDTO lancamentoRetornado = this.service.buscarPorId(1L);
		
		assertEquals(lancamento.getId(), lancamentoRetornado.getId());
		
	}
	
	@Test
	void obterListaLancamentoObterListaVazia() {
		
		Categoria categoria = new Categoria();
		
		Balanco balanco = new Balanco();
		
		balanco.setCategoria(categoria);
		
		Mockito.when(balancoService.buscarPorId(Mockito.anyLong())).thenReturn(balanco);
		
		PageRequest pageable = PageRequest.of(0,4,Direction.ASC,"id");
		
		Mockito.when(repository.findByBalanco(Mockito.any(),Mockito.any()))
			.thenReturn(new PageImpl<>(new ArrayList<>(),pageable,4));
		
		Page<LancamentoDTO> pagLancamentos = this.service.listar(1L, 0, 4, 1);
		
		assertEquals(0, pagLancamentos.getPageable().getPageNumber());
		assertEquals(4, pagLancamentos.getPageable().getPageSize());
		assertTrue(pagLancamentos.isEmpty());
		
	}
	
	@Test
	void obterListaLancamentoObterErroDeQuantidadeInvalida() {
		
		assertThrows(IllegalParameterException.class, () -> this.service.listar(1L, 0, -1, 1));
		
	}
	
	@Test
	void obterListaLancamentoObterErroDePaginaInvalida() {
		
		assertThrows(IllegalParameterException.class, () -> this.service.listar(1L, -2, 4, 1));
		
	}
	
	@Test
	void obterListaLancamentosConseguirLista() {
		
		Categoria categoria = new Categoria();
		
		Balanco balanco = new Balanco();
		
		balanco.setCategoria(categoria);
		
		List<Lancamento> lancamentos = getLancamentos();
		
		lancamentos.forEach(l -> l.setBalanco(balanco));
		
		Mockito.when(balancoService.buscarPorId(Mockito.anyLong())).thenReturn(balanco);
		
		PageRequest pageable = PageRequest.of(0,4,Direction.ASC,"id");
		
		Mockito.when(repository.findByBalanco(Mockito.any(),Mockito.any()))
			.thenReturn(new PageImpl<>(lancamentos,pageable,4));
		
		Page<LancamentoDTO> paginaLancamento = this.service.listar(1L, 0, 4, 1);
		
		assertEquals(0, paginaLancamento.getPageable().getPageNumber());
		assertEquals(4, paginaLancamento.getPageable().getPageSize());
		assertTrue(paginaLancamento.getSort().isSorted());
		assertFalse(paginaLancamento.isEmpty());
		
	}
	
	private List<Lancamento> getLancamentos(){
		
		List<Lancamento> lancamentos = new ArrayList<>();
		
		TipoLancamento tipoProvento = new TipoLancamento(1L,TipoLancamentoEnum.PROVENTO.getValor(),
				TipoLancamentoEnum.PROVENTO.getNome());
		
		TipoLancamento tipoDespesa = new TipoLancamento(2L,TipoLancamentoEnum.DESPESA.getValor(),
				TipoLancamentoEnum.DESPESA.getNome());
		
		lancamentos.add(new Lancamento(1L,"Test 1", "Testando...", new BigDecimal("500"), LocalDate.now(),
				LocalDateTime.now(),tipoProvento));
		
		lancamentos.add(new Lancamento(2L,"Test 2", "Testando...", new BigDecimal("200"), LocalDate.now(),
				LocalDateTime.now(),tipoProvento));
		
		lancamentos.add(new Lancamento(3L,"Test 3", "Testando...", new BigDecimal("50"), LocalDate.now(),
				LocalDateTime.now(),tipoDespesa));
		
		lancamentos.add(new Lancamento(4L,"Test 4", "Testando...", new BigDecimal("70"), LocalDate.now(),
				LocalDateTime.now(),tipoDespesa));
		
		return lancamentos;
	}

}
