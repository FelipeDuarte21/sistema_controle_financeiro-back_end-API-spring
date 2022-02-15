package br.com.felipeduarte.APIControleFinanceiro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.LocalDate;
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

import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.CategoriaDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.CategoriaSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.CategoriaRepository;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

class CategoriaServiceTest {
	
	private CategoriaService service;
	
	@Mock
	private CategoriaRepository categoriaRepository;
	
	@Mock
	private BalancoService balancoService;
	
	@Mock
	private RestricaoService restricaoService;
	
	@Mock
	private Clock clock;
	
	@Captor
	private ArgumentCaptor<Categoria> captor;

	@BeforeEach
	private void inicialiar() {
		MockitoAnnotations.openMocks(this);
		this.service = new CategoriaService(categoriaRepository,balancoService,restricaoService,clock);
		Mockito.when(restricaoService.getUsuario()).thenReturn(getUsuario());
	}
	
	@Test
	void salvarCategoriaObterErroDeCategoriaJaCadastrada() {
		
		Mockito.when(categoriaRepository.findByNomeAndUsuario(Mockito.anyString(), Mockito.any()))
			.thenReturn(Optional.of(getCategorias().get(0)));
		
		CategoriaSalvarDTO categoria = new CategoriaSalvarDTO("Teste","Testando o metodo salvar");
		
		assertThrows(IllegalParameterException.class, () -> this.service.salvar(categoria));
		
	}
	
	@Test
	void salvarCategoriaConseguirSalvar() {
		
		CategoriaSalvarDTO categoria = new CategoriaSalvarDTO("Teste","Testando o metodo salvar");
		
		Mockito.when(categoriaRepository.findByNomeAndUsuario(Mockito.anyString(),Mockito.any()))
			.thenReturn(Optional.empty());
		
		Categoria categoriaPosSalva = new Categoria(4L,"Teste","Testando o metodo salvar", LocalDate.now());
		
		Mockito.when(categoriaRepository.save(Mockito.any())).thenReturn(categoriaPosSalva);
		
		Mockito.when(clock.getZone()).thenReturn(ZoneId.of("America/Sao_Paulo"));
		
		CategoriaDTO categoriaSalva = this.service.salvar(categoria);
		
		assertEquals(categoriaPosSalva.getNome(), categoriaSalva.getNome());
		assertEquals(categoriaPosSalva.getDescricao(),categoriaSalva.getDescricao());
		assertEquals(categoriaPosSalva.getDataCadastro(), categoriaSalva.getDataCadastro());
		assertEquals(4L, categoriaSalva.getId());
		
	}
	
	@Test
	void atualizarCategoriaObterErroDeCategoriaNaoEncontrada() {
		
		Mockito.when(categoriaRepository.findById(Mockito.anyLong()))
			.thenReturn(Optional.empty());
		
		CategoriaSalvarDTO categoria = new CategoriaSalvarDTO("Despesas Gerais",
				"Destinado a despesas gerais do mes, tais como cartão de crédito e etc.");
		
		assertThrows(ObjectNotFoundFromParameterException.class, 
				() -> this.service.atualizar(1L, categoria));
		
	}
	
	@Test
	void atualizarCategoriaObterErroDeIdIgualAZero() {
		
		CategoriaSalvarDTO categoria = new CategoriaSalvarDTO("Despesas Gerais",
				"Destinado a despesas gerais do mes, tais como cartão de crédito e etc.");
		
		assertThrows(IllegalParameterException.class, () -> this.service.atualizar(0L, categoria));
		
	}
	
	@Test
	void atualizarCategoriaObterErroDeIdNullo() {
		
		CategoriaSalvarDTO categoria = new CategoriaSalvarDTO("Despesas Gerais",
				"Destinado a despesas gerais do mes, tais como cartão de crédito e etc.");
		
		assertThrows(IllegalParameterException.class, () -> this.service.atualizar(null, categoria));
		
	}
	
	@Test
	void atualizarCategoriaConseguirAtualizar() {
		
		CategoriaSalvarDTO categoria = new CategoriaSalvarDTO("Despesas Gerais",
				"Destinado a despesas gerais do mes, tais como cartão de crédito e etc.");
		
		Mockito.when(categoriaRepository.findById(Mockito.anyLong()))
			.thenReturn(Optional.of(getCategorias().get(0)));
		
		Categoria categoriaPosAtualizada = new Categoria(4L,"Despesas Gerais",
				"Destinado a despesas gerais do mes, tais como cartão de crédito e etc.", LocalDate.now());
		
		Mockito.when(categoriaRepository.save(Mockito.any())).thenReturn(categoriaPosAtualizada);
		
		CategoriaDTO categoriaAtualizada = this.service.atualizar(1L, categoria);
		
		assertEquals(categoriaPosAtualizada.getNome(), categoriaAtualizada.getNome());
		assertEquals(categoriaAtualizada.getDescricao(), categoriaAtualizada.getDescricao());
		
	}
	
	@Test
	void excluirCategoriaObterErroDeCategoriaNaoEncontrada() {
		
		Mockito.when(categoriaRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
	
		assertThrows(ObjectNotFoundFromParameterException.class, () -> this.service.excluir(1L));
		
	}
	
	@Test
	void excluirCategoriaConseguirExcluir() {
		
		Mockito.when(categoriaRepository.findById(Mockito.anyLong()))
			.thenReturn(Optional.of(getCategorias().get(0)));
		
		this.service.excluir(1L);
		
		Mockito.verify(categoriaRepository).delete(captor.capture());
		
		Categoria categoria = captor.getValue();
		
		assertEquals(1L, categoria.getId());
		assertEquals("Despesas Gerais", categoria.getNome());
		
	}
	
	@Test
	void obterCategoriaPeloIdLancarErroDeCategoriaNaoEncontrada() {
		
		Mockito.when(categoriaRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		
		assertThrows(ObjectNotFoundFromParameterException.class, () -> this.service.buscarPorId(1L));
		
	}
	
	@Test
	void obterCategoriaPeloIdRetornarCategoria() {
		
		Mockito.when(categoriaRepository.findById(Mockito.any()))
			.thenReturn(Optional.of(getCategorias().get(0)));
		
		CategoriaDTO categoria = this.service.buscarPorId(1L);
		
		assertEquals(1L, categoria.getId());
		assertEquals("Despesas Gerais", categoria.getNome());
		
	}
	
	@Test
	void obterListaCategoriaPeloUsuarioLancarErroDeListaVazia() {
		
		Mockito.when(categoriaRepository.findByUsuario(Mockito.any(Usuario.class)))
			.thenReturn(new ArrayList<>());
		
		assertThrows(ObjectNotFoundFromParameterException.class, 
				() -> this.service.buscarPorUsuario(getUsuario()));
		
	}
	
	@Test
	void obterListaCategoriaPeloUsuarioRetornarListaDeCategorias() {
		
		Mockito.when(categoriaRepository.findByUsuario(Mockito.any(Usuario.class)))
			.thenReturn(getCategorias());
		
		List<CategoriaDTO> categorias = this.service.buscarPorUsuario(getUsuario());
				
		assertFalse(categorias.isEmpty());
		
	}
	
	@Test
	void obterPaginaCategoriaLancarErroNoParametroQuantidade() {
		assertThrows(IllegalParameterException.class, () -> this.service.listar(0, -3, 1));
	}
	
	@Test
	void obterPaginaCategoriaLancarErroNoParametroPagina() {
		assertThrows(IllegalParameterException.class,() -> this.service.listar(-1, 3, 1));
	}
	
	@Test
	void obterPaginaCategoriaPaginaSemConteudo() {
		
		PageRequest pageable = PageRequest.of(0, 3, Direction.ASC, "nome");
		
		Mockito.when(categoriaRepository.findByUsuario(
				Mockito.any(Usuario.class), Mockito.any(PageRequest.class)))
					.thenReturn(new PageImpl<Categoria>(new ArrayList<>(),pageable,6));
		
		Page<CategoriaDTO> pageCategoria = this.service.listar(0, 3, 1);
		
		assertEquals(0, pageCategoria.getPageable().getPageNumber());
		assertEquals(3, pageCategoria.getPageable().getPageSize());
		assertTrue(pageCategoria.getContent().isEmpty());
		
	}
	
	@Test
	void obterPaginaCategoriaRetornarPaginaComConteudo() {
		
		PageRequest pageable = PageRequest.of(0, 3, Direction.ASC, "nome");
		
		Mockito.when(categoriaRepository.findByUsuario(
				Mockito.any(Usuario.class), Mockito.any(PageRequest.class)))
					.thenReturn(new PageImpl<Categoria>(getCategorias(),pageable,6));
		
		Page<CategoriaDTO> pageCategoria = this.service.listar(0, 3, 1);
		
		assertEquals(0, pageCategoria.getPageable().getPageNumber());
		assertEquals(3,pageCategoria.getPageable().getPageSize());
		assertFalse(pageCategoria.getContent().isEmpty());
		assertTrue(pageCategoria.getSort().isSorted());
		
	}
	
	private List<Categoria> getCategorias(){
		
		var listaCategorias = new ArrayList<Categoria>();
		
		listaCategorias.add(new Categoria(1L,"Despesas Gerais","Destinado a despesas gerais do mes", LocalDate.now()));
		listaCategorias.add(new Categoria(2L,"Investimentos","Fundo reservado para investimentos",LocalDate.now()));
		listaCategorias.add(new Categoria(3L,"Doações","Fundo reservado para doações",LocalDate.now()));
		
		return listaCategorias;
	}
	
	private Usuario getUsuario() {
		return new Usuario(1L,"Luiz","luiz@gmail.com","12345678");
	}

}
