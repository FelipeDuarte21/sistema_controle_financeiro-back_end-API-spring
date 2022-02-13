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
	
	private List<Usuario> getListaUsuarios(){
		
		var listaUsuarios = new ArrayList<Usuario>();
		
		listaUsuarios.add(new Usuario(1L,"Luiz","luiz@gmail.com","123456"));
		listaUsuarios.add(new Usuario(2L,"Joao","joa@gmail.com","123456"));
		listaUsuarios.add(new Usuario(3L,"Marcos","marcos@gmail.com","123456"));
		
		return listaUsuarios;
	}
	
}
