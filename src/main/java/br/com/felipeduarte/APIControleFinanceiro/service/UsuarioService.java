package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.UsuarioDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.UsuarioSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.enums.TipoUsuario;
import br.com.felipeduarte.APIControleFinanceiro.repository.UsuarioRepository;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

@Service
public class UsuarioService {
	
	private static final Pattern VALIDADOR_EMAIL = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	
	private UsuarioRepository repository;
	
	private BCryptPasswordEncoder bCrypt;
	
	private RestricaoService restricaoService;
	
	@Autowired
	public UsuarioService(UsuarioRepository repository, BCryptPasswordEncoder bCrypt,
			RestricaoService restricaoService) {
		this.repository = repository;
		this.bCrypt = bCrypt;
		this.restricaoService = restricaoService;
	}

	public UsuarioDTO salvar(UsuarioSalvarDTO usuarioDTO) {
		
		var optUsuarioEmail = this.repository.findByEmail(usuarioDTO.getEmail());
		
		if(optUsuarioEmail.isPresent()) throw new IllegalParameterException("Erro! email já está em uso!");
		
		var usuario = new Usuario(usuarioDTO);
		usuario.setSenha(this.bCrypt.encode(usuarioDTO.getSenha()));
		usuario.addTipo(TipoUsuario.USUARIO.getCodigo());
		
		usuario = this.repository.save(usuario);
		
		return new UsuarioDTO(usuario);
		
	}
	
	public UsuarioDTO atualizar(Long id, UsuarioSalvarDTO usuarioDTO) {
		
		if(id == null) throw new IllegalParameterException("Erro! id não pode ser nullo");
		if(id == 0) throw new IllegalParameterException("Erro! id não pode ser 0");
		
		//Verifica se usuario é o mesmo que está logado
		this.restricaoService.verificarUsuario(id);
		
		var optUsuario = this.repository.findById(id);
		
		if(!optUsuario.isPresent())
			throw new ObjectNotFoundFromParameterException(
					"Erro! usuário não encontrado para o id informado!");
		
		//Verifica se novo email já esteja cadastrado!
		
		var optUsuarioEmail = this.repository.findByEmail(usuarioDTO.getEmail());
		
		if(optUsuarioEmail.isPresent() && !optUsuarioEmail.get().getId().equals(optUsuario.get().getId()) )
			throw new IllegalParameterException("Erro! email já está em uso!");
		
		
		var usuario = optUsuario.get();
		usuario.setNome(usuarioDTO.getNome());
		usuario.setEmail(usuarioDTO.getEmail());
		usuario.setSenha(bCrypt.encode(usuarioDTO.getSenha()));
		
		usuario = this.repository.save(usuario);
		
		return new UsuarioDTO(usuario);
	}
	
	public void excluir(Long id) {
		
		var optUsuario = this.repository.findById(id);
		
		if(!optUsuario.isPresent())
			throw new ObjectNotFoundFromParameterException(
					"Erro! usuário não encontrado para o id informado!");
		
		//Verifica se usuario é o mesmo que está logado
		this.restricaoService.verificarUsuario(id);
		
		this.repository.delete(optUsuario.get());
		
	}
	
	public UsuarioDTO buscarPorId(Long id) {
		
		var optUsuario =  this.repository.findById(id);
		
		if(!optUsuario.isPresent()) 
			throw new ObjectNotFoundFromParameterException("Erro! usuario não encontrado para id informado!");
		
		return new UsuarioDTO(optUsuario.get());
		
	}
	
	public UsuarioDTO buscarPorEmail(String email,boolean verificaUsuarioLogado) {
		
		if(!VALIDADOR_EMAIL.matcher(email).find())
			throw new IllegalParameterException("Erro! o email está fora do padrão!");
		
		var optUsuario = this.repository.findByEmail(email);
		
		if(!optUsuario.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! usuario não encontrado para o email informado!");
		
		if(verificaUsuarioLogado) 
			this.restricaoService.verificarUsuario(optUsuario.get().getId());
		
		return new UsuarioDTO(optUsuario.get());
		
	}
	
	public Page<UsuarioDTO> listar(int page, int size, int order){
		
		if(page < 0) 
			throw new IllegalParameterException("Erro! o número da página não pode ser negativo!");
		
		if(size < 1) 
			throw new IllegalParameterException("Erro! a quantidade de elementos na página é no mínimo 1");
		
		var direcao = Direction.ASC;
		
		if(order == 2) direcao = Direction.DESC;
		
		var pageable = PageRequest.of(page, size, direcao, "nome");
		
		var pageUsuarios = this.repository.findAll(pageable);
		
		var pageUsuarioDTO = new PageImpl<UsuarioDTO>(
				pageUsuarios.getContent().stream().map(UsuarioDTO::new).collect(Collectors.toList()),
					pageUsuarios.getPageable(),pageUsuarios.getTotalElements());
		
		return pageUsuarioDTO;
		
	}
	
}
