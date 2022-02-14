package br.com.felipeduarte.APIControleFinanceiro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import br.com.felipeduarte.APIControleFinanceiro.model.dto.UsuarioSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.UsuarioRepository;
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
	void salvarUsuarioLancarErroDeEmailEmUsoPorOutroUsuario() {
		
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString()))
			.thenReturn(Optional.of(getListaUsuarios().get(0)));
		
		UsuarioSalvarDTO usuario = new UsuarioSalvarDTO("Luiz","luiz@gmail.com","12345678");
		
		assertThrows(IllegalParameterException.class, () -> this.usuarioService.salvar(usuario));
		
	}
	
	@Test
	void salvarUsuarioConseguirSalvar() {
		
		UsuarioSalvarDTO usuario = new UsuarioSalvarDTO("Zezinho","zezinho@gmail.com","12345678");
		
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		Usuario usuarioPosSalvo = new Usuario(usuario);
		usuarioPosSalvo.setId(1L);
		
		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuarioPosSalvo);
		
		Mockito.when(bCrypt.encode(usuario.getSenha())).thenReturn(usuario.getSenha());
		
		this.usuarioService.salvar(usuario);
		
		Mockito.verify(usuarioRepository).save(captor.capture());
		
		Usuario usuarioSalvo = captor.getValue();
		
		assertEquals(usuario.getNome(), usuarioSalvo.getNome());
		assertEquals(usuario.getEmail(),usuarioSalvo.getEmail());
		assertEquals(usuario.getSenha(), usuarioSalvo.getSenha());
	}
	
	@Test
	void atualizarUsuarioLancarErroDeEmailJaUsadoPorOutroUsuario() {
		
		UsuarioSalvarDTO usuario = new UsuarioSalvarDTO("Luiz","joao@gmail.com","12345678");
		
		Mockito.when(usuarioRepository.findById(Mockito.anyLong()))
			.thenReturn(Optional.of(getListaUsuarios().get(0)));
		
		
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString()))
			.thenReturn(Optional.of(getListaUsuarios().get(1)));
		
		assertThrows(IllegalParameterException.class, () -> this.usuarioService.atualizar(1L, usuario));
		
	}
	
	@Test
	void atualizarUsuarioLancarErroDeUsuarioNaoEncontrado() {
		
		Mockito.when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		UsuarioSalvarDTO usuario = new UsuarioSalvarDTO("Zezinho","zezinho@gmail.com","12345678");
		
		assertThrows(ObjectNotFoundFromParameterException.class,() -> 
			this.usuarioService.atualizar(1L,usuario));
		
	}
	
	@Test
	void atualizarUsuarioLancarErroDeIdZero() {
		
		UsuarioSalvarDTO usuario = new UsuarioSalvarDTO("Zezinho","zezinho@gmail.com","12345678");
		
		assertThrows(IllegalParameterException.class, () -> this.usuarioService.atualizar(0L, usuario));
		
	}
	
	@Test
	void atualizarUsuarioLancarErroDeIdNullo() {
		
		UsuarioSalvarDTO usuario = new UsuarioSalvarDTO("Zezinho","zezinho@gmail.com","12345678");
		
		assertThrows(IllegalParameterException.class, () -> this.usuarioService.atualizar(null, usuario));
		
	}
	
	@Test
	void atualizarUsuarioConseguirAtualizar() {
		
		UsuarioSalvarDTO usuario = new UsuarioSalvarDTO("Luiz Felipe","luizfelipe@gmail.com","abcdefghi");
		
		Mockito.when(usuarioRepository.findById(Mockito.anyLong()))
			.thenReturn(Optional.of(getListaUsuarios().get(0)));
		
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		Usuario usuarioPosAtualizado = new Usuario(usuario);
		usuarioPosAtualizado.setId(1L);
		
		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuarioPosAtualizado);
		
		Mockito.when(bCrypt.encode(usuario.getSenha())).thenReturn(usuario.getSenha());
		
		this.usuarioService.atualizar(1L, usuario);
		
		Mockito.verify(usuarioRepository).save(captor.capture());
		
		Usuario usuarioAtualizado = captor.getValue();
		
		assertEquals("Luiz Felipe", usuarioAtualizado.getNome());
		assertEquals("luizfelipe@gmail.com", usuarioAtualizado.getEmail());
		assertEquals("abcdefghi", usuarioAtualizado.getSenha());
	
	}
	
	@Test
	void excluirUsuarioPeloIdLancarErroDeUsuarioNaoEncontrado() {
		
		Mockito.when(usuarioRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		assertThrows(ObjectNotFoundFromParameterException.class, () -> this.usuarioService.excluir(1L));
		
	}
	
	@Test
	void excluirUsuarioPeloIdConseguirExcluir() {
		
		Usuario usuario = getListaUsuarios().get(0);
		
		Mockito.when(usuarioRepository.findById(Mockito.anyLong()))
			.thenReturn(Optional.of(usuario));
		
		Mockito.when(restricaoService.getUsuario()).thenReturn(usuario);
		
		this.usuarioService.excluir(1L);
		
		Mockito.verify(usuarioRepository).delete(captor.capture());
		
		Usuario usuarioExcluido = captor.getValue();
		
		assertEquals(usuario.getId(), usuarioExcluido.getId());
		assertEquals(usuario.getEmail(), usuarioExcluido.getEmail());
		assertEquals(usuario.getNome(), usuarioExcluido.getNome());
		
	}
	
	@Test
	void obterUsuarioPeloEmailLancarErroDeEmailForaDoPadrao() {
		assertThrows(IllegalParameterException.class,() -> 
			this.usuarioService.buscarPorEmail("loremispsumisdolorskdjskd#$sdk", true));
	}
	
	@Test
	void obterUsuarioPeloEmailLancarErroDeUsuarioNaoEncotrado() {
		
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		Mockito.when(restricaoService.getUsuario()).thenReturn(getListaUsuarios().get(0));
		
		assertThrows(ObjectNotFoundFromParameterException.class,() -> 
			this.usuarioService.buscarPorEmail("luiz@gmail.com", true));
		
	}
	
	@Test
	void obterUsuarioPeloEmailRetornarOUsuarioEncotrado() {
		
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString()))
			.thenReturn(Optional.of(getListaUsuarios().get(0)));
		
		Mockito.when(restricaoService.getUsuario()).thenReturn(getListaUsuarios().get(0));
		
		UsuarioDTO usuario = this.usuarioService.buscarPorEmail("luiz@gmail.com", true);
		
		assertEquals("luiz@gmail.com", usuario.getEmail());
		
	}
	
	@Test
	void obterPaginaUsuarioLancarErroNoParametroSize() {
		assertThrows(IllegalParameterException.class, () -> this.usuarioService.listar(0, 0, 1) );
	}
	
	@Test
	void obterPaginaUsuarioLancarErroNoParametroPagina() {
		assertThrows(IllegalParameterException.class, () -> this.usuarioService.listar(-1, 3, 1) );
	}
	
	@Test
	void obterPaginaUsuarioRetornarPaginaSemConteudo() {
		
		PageRequest pageable = PageRequest.of(0, 3,Direction.ASC,"nome");
		
		Mockito.when(usuarioRepository.findAll(Mockito.any(PageRequest.class)))
			.thenReturn(new PageImpl<>(new ArrayList<>(),pageable,6));
		
		Page<UsuarioDTO> paginaUsuario = this.usuarioService.listar(0, 3, 1);
		
		assertEquals(0, paginaUsuario.getPageable().getPageNumber());
		assertEquals(3, paginaUsuario.getPageable().getPageSize());
		assertTrue(paginaUsuario.getSort().isSorted());
		assertTrue(paginaUsuario.getContent().isEmpty());
		
	}
	
	@Test
	void obterPaginaUsuarioRetornarPaginaComConteudo() {
		
		PageRequest pageable = PageRequest.of(0, 3,Direction.ASC,"nome");
		
		Mockito.when(usuarioRepository.findAll(Mockito.any(PageRequest.class)))
			.thenReturn(new PageImpl<>(getListaUsuarios(),pageable,6));
		
		Page<UsuarioDTO> paginaUsuario = this.usuarioService.listar(0, 3, 1);
		
		assertEquals(0, paginaUsuario.getPageable().getPageNumber());
		assertEquals(3, paginaUsuario.getPageable().getPageSize());
		assertTrue(paginaUsuario.getSort().isSorted());
		assertFalse(paginaUsuario.getContent().isEmpty());
		
	}
	
	private List<Usuario> getListaUsuarios(){
		
		var listaUsuarios = new ArrayList<Usuario>();
		
		listaUsuarios.add(new Usuario(1L,"Luiz","luiz@gmail.com","123456"));
		listaUsuarios.add(new Usuario(2L,"Joao","joao@gmail.com","123456"));
		listaUsuarios.add(new Usuario(3L,"Marcos","marcos@gmail.com","123456"));
		
		return listaUsuarios;
	}
	
}
