package br.com.felipeduarte.APIControleFinanceiro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.felipeduarte.APIControleFinanceiro.model.TipoLancamento;
import br.com.felipeduarte.APIControleFinanceiro.repository.TipoLancamentoRepository;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

class TipoLancamentoServiceTest {
	
	private TipoLancamentoService service;
	
	@Mock
	private TipoLancamentoRepository tipoLancamentoRepository;
	
	@BeforeEach
	private void inicializar() {
		MockitoAnnotations.openMocks(this);
		this.service = new TipoLancamentoService(tipoLancamentoRepository);
	}
	
	@Test
	void buscarPorValorLancarErroDeTipoNaoEncontrado() {
		
		Mockito.when(tipoLancamentoRepository.findByValor(Mockito.anyInt()))
			.thenReturn(Optional.empty());
		
		assertThrows(ObjectNotFoundFromParameterException.class, 
				() -> this.service.buscarPorValor(1));
		
	}

	@Test
	void buscarPorValorRetornarTipoLancamento() {
		
		TipoLancamento tipo = new TipoLancamento(1L,0,"Provento");
		
		Mockito.when(tipoLancamentoRepository.findByValor(Mockito.anyInt()))
			.thenReturn(Optional.of(tipo));
		
		TipoLancamento tipoRetornado = this.service.buscarPorValor(1);
		
		assertEquals(1L, tipoRetornado.getId());
		assertEquals("Provento", tipoRetornado.getNome());
		assertEquals(0, tipoRetornado.getValor());
		
	}

}
