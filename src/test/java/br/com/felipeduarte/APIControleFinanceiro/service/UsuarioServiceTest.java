package br.com.felipeduarte.APIControleFinanceiro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

class UsuarioServiceTest {

	private UsuarioService usuarioService;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Mock
	private BCryptPasswordEncoder bCrypt;
	
	@Mock
	private RestricaoService restricaoService;
	
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
	void retornandoPaginaDeUsuarioConformeONumeroDaPaginaAQuantidadeDeUsuariosEOrdenacaoDosRegistros() {
		
		PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "nome");
		
		Mockito.when( usuarioRepository.findAll( pageRequest ) )
			.thenReturn(new PageImpl<Usuario>( getListaUsuarios(), pageRequest, 3));
		
		Page<UsuarioDTO> pageUsuarios = this.usuarioService.listar(0, 3, 1);
		
		assertEquals(0, pageUsuarios.getNumber());
		assertEquals(3, pageUsuarios.getTotalElements());
		assertTrue(pageUsuarios.getSort().isSorted());
		
	}
	
	@Test
	void retornandoPaginaDeUsuariosVazia() {
		
		PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "nome");
		
		Mockito.when( usuarioRepository.findAll( pageRequest ) )
			.thenReturn(new PageImpl<Usuario>(new ArrayList<>()));
		
		Page<UsuarioDTO> pageUsuarios = this.usuarioService.listar(0, 3, 1);
		
		assertEquals(0, pageUsuarios.getTotalElements());
		assertEquals(0, pageUsuarios.getContent().size());
		
	}
	
	private List<Usuario> getListaUsuarios(){
		
		var listaUsuarios = new ArrayList<Usuario>();
		
		listaUsuarios.add(new Usuario(1L,"Luiz","luiz@gmail.com","123456"));
		listaUsuarios.add(new Usuario(2L,"Luiz","luiz@gmail.com","123456"));
		listaUsuarios.add(new Usuario(3L,"Luiz","luiz@gmail.com","123456"));
		
		return listaUsuarios;
	}
	
}
