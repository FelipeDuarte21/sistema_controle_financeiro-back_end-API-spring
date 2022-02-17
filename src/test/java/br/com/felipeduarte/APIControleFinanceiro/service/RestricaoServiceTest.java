package br.com.felipeduarte.APIControleFinanceiro.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.repository.CategoriaRepository;
import br.com.felipeduarte.APIControleFinanceiro.repository.UsuarioRepository;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.AuthorizationException;

class RestricaoServiceTest {
	
	private RestricaoService restricaoService;
	
	@Mock
	private CategoriaRepository categoriaRepository;
	
	@Mock
	private UsuarioRepository usuarioRepository;

	@BeforeEach
	private void inicializar() {
		MockitoAnnotations.openMocks(this);
		this.restricaoService = new RestricaoService(usuarioRepository, categoriaRepository);
	}
	
	@Test
	void getUsuarioLancarErroDeUsuarioNaoLogado() {
		
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertThrows(AuthorizationException.class, () -> this.restricaoService.getUsuario());
		
	}
	
	@Test
	void verificarPermissaoConteudoLancarErroDeAcessoInvalidoCategoriaNaoForEncontrada() {
		
		List<Categoria> categorias = new ArrayList<>();
		categorias.add(new Categoria(1L,"Teste 1","Testando....",LocalDate.now()));
		categorias.add(new Categoria(2L,"Teste 2","Testando....",LocalDate.now()));
		
		Mockito.when(categoriaRepository.findByUsuario(Mockito.any())).thenReturn(categorias);
		
		Categoria categoria = new Categoria(3L,"Teste 3", "Testando....",LocalDate.now());
		
		assertThrows(AuthorizationException.class, () -> 
			this.restricaoService.verificarPermissaoConteudo(categoria));
		
	}
	
	@Test
	void verificarPermissaoConteudoLancarErroDeAcessoInvalidoQuandoUsuarioNaoTemCategorias() {
		
		Mockito.when(categoriaRepository.findByUsuario(Mockito.any())).thenReturn(new ArrayList<>());
		
		assertThrows(AuthorizationException.class, () -> 
			this.restricaoService.verificarPermissaoConteudo(new Categoria()));
		
	}
	
	@Test
	void verificarUsuarioLancarErroDeAcessoInvalido() {
		
		Usuario usuario = new Usuario(1L,"Luiz","luiz@gmail.com","12345678");
		
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString()))
			.thenReturn(Optional.of(usuario));
		
		assertThrows(AuthorizationException.class, () -> this.restricaoService.verificarUsuario(2L));
		
	}

}
