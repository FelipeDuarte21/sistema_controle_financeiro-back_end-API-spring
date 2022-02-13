package br.com.felipeduarte.APIControleFinanceiro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.UsuarioDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.UsuarioRepository;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.AuthorizationException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

class UsuarioServiceTest {

	private UsuarioService usuarioService;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Mock
	private BCryptPasswordEncoder bCrypt;
	
	@Mock
	private RestricaoService restricaoService;
	
	@Captor
	private ArgumentCaptor<Usuario> captor;
	
	@BeforeEach
	void inicializar() {
		MockitoAnnotations.openMocks(this);
		this.usuarioService = new UsuarioService(usuarioRepository,bCrypt,restricaoService);
	}

	/* Teste de verificação de veracidade dos testes
	@Test
	void testandoFuncionamentoDoTeste0Igual0() {
		assertEquals(0, 0);
	}
	
	@Test
	void testandoFuncionamentoDoTeste0DiferenteDe0() {
		assertNotEquals(0, 0);
	}*/
	
	@Test
	void excluindoUsuarioPeloIdQuandoUsuarioNaoForOUsuarioDoIdInformadoDeveLancarException() {
		
		Mockito.when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(
				Optional.of(getListaUsuarios().get(0)));

		Mockito.when(restricaoService.verificarUsuario(Mockito.anyLong()))
			.thenThrow(AuthorizationException.class);
		
		assertThrows(AuthorizationException.class, () -> this.usuarioService.excluir(1L));
		
	}
	
	@Test
	void excluindoUsuarioPeloIdQuandoOProprioUsuarioEstiverLogadoDeveNaoConseguirExcluirQuandoIdNaoForEncontrado() {
		
		Mockito.when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		assertThrows(ObjectNotFoundFromParameterException.class,() -> this.usuarioService.excluir(1L));
		
	}
	
	@Test
	void excluindoUsuarioPeloIdQuandoOProprioUsuarioEstiverLogadoDeveConseguirExcluirQuandoIdForEncontrado() {
		
		Mockito.when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(
				Optional.of(getListaUsuarios().get(0)));
		
		Mockito.when(restricaoService.getUsuario()).thenReturn(getListaUsuarios().get(0));
		
		try {
			
			this.usuarioService.excluir(1L);
			
			Mockito.verify(usuarioRepository).delete(captor.capture());
			
			Usuario usuarioCapturado = captor.getValue();
			
			assertEquals(1L, usuarioCapturado.getId());
			
			
		}catch(IllegalParameterException e) {
			fail("O usuario não foi encontrado pelo id");
			
		}
		
	}
	
	@Test
	void buscandoUsuarioPeloIdDeveRetornarOUsuario() {
		
		Mockito.when(usuarioRepository.findById(Mockito.anyLong()))
			.thenReturn(Optional.of(getListaUsuarios().get(0)));
		
		Optional<UsuarioDTO> optUsuario = this.usuarioService.buscarPorId(1L);
		
		assertTrue(optUsuario.isPresent());
		assertEquals(1L, optUsuario.get().getId());
		
	}
	
	@Test
	void buscandoUsuarioPeloIdDeveRetornarUsuarioInexistente() {
		
		Mockito.when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Optional<UsuarioDTO> optUsuario = this.usuarioService.buscarPorId(1L);
		
		assertFalse(optUsuario.isPresent());
		
	}
	
	@Test
	void buscandoUsuarioPeloEmailSemVerificaoDeUsuarioLogadoDeveRetornarOUsuario() {
			
		Mockito.when(usuarioRepository.findByEmail(Mockito.any()))
			.thenReturn(Optional.of(getListaUsuarios().get(0)));
		
		Optional<UsuarioDTO> optUsuario = this.usuarioService.buscarPorEmail("luiz@gmail.com", false);
		
		assertTrue(optUsuario.isPresent());
		assertEquals("luiz@gmail.com", optUsuario.get().getEmail());
	}
	
	@Test
	void buscandoUsuarioPeloEmailSemVerificacaoDeUsuarioLogadoDeveRetornarUsuarioInexistente() {
		
		Mockito.when(usuarioRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
		
		Optional<UsuarioDTO> usuario = this.usuarioService.buscarPorEmail("luiz@gmail.com", false);
		
		assertFalse(usuario.isPresent());
		
	}
	
	void buscandoUsuarioPeloEmailVerificandoSeUsuarioEstaLogadoDeveLancarException() {
		
		Mockito.when(usuarioRepository.findByEmail(Mockito.any()))
			.thenReturn(Optional.of(getListaUsuarios().get(0)));
		
		assertThrows(AuthorizationException.class, () -> 
			this.usuarioService.buscarPorEmail("luiz@gmail.com", true) );
		
	}
	
	void buscandoUsuarioPeloEmailVerificandoSeUsuarioEstaLogadoDeveRetornarOUsuario() {
		
		Mockito.when(restricaoService.getUsuario()).thenReturn(getListaUsuarios().get(0));
		
		Optional<UsuarioDTO> optUsuario = this.usuarioService.buscarPorEmail("luiz@gmail.com", true);
		
		assertNotNull(optUsuario.get());
		assertEquals("luiz@gmail.com", optUsuario.get().getEmail());
		
	}
	
	@Test
	void buscandoPaginaDeUsuarioDeveRetornarNumeroDaPaginaQuantidadeDeUsuariosOrdenacao() {
		
		Mockito.when( usuarioRepository.findAll( Mockito.any(PageRequest.class) ) )
			.thenReturn(new PageImpl<Usuario>( getListaUsuarios(), 
					PageRequest.of(0, 3, Direction.ASC, "nome"), 3) );
		
		Page<UsuarioDTO> pageUsuarios = this.usuarioService.listar(0, 3, 1);
		
		assertEquals(0, pageUsuarios.getNumber());
		assertEquals(3, pageUsuarios.getTotalElements());
		assertTrue(pageUsuarios.getSort().isSorted());
		
	}
	
	@Test
	void buscandoPaginaDeUsuariosDeveRetornarPaginaVazia() {
		
		Mockito.when( usuarioRepository.findAll( Mockito.any(PageRequest.class) ) )
			.thenReturn(new PageImpl<Usuario>(new ArrayList<>()));
		
		Page<UsuarioDTO> pageUsuarios = this.usuarioService.listar(0, 3, 1);
		
		assertEquals(0, pageUsuarios.getTotalElements());
		assertEquals(0, pageUsuarios.getContent().size());
		
	}
	
	private List<Usuario> getListaUsuarios(){
		
		var listaUsuarios = new ArrayList<Usuario>();
		
		listaUsuarios.add(new Usuario(1L,"Luiz","luiz@gmail.com","123456"));
		listaUsuarios.add(new Usuario(2L,"Joao","joa@gmail.com","123456"));
		listaUsuarios.add(new Usuario(3L,"Marcos","marcos@gmail.com","123456"));
		
		return listaUsuarios;
	}
	
}
