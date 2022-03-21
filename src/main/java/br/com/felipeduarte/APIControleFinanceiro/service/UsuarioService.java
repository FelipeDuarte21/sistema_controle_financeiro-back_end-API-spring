package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.UsuarioAtualizarDTO;
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

	@Transactional(rollbackOn = Exception.class)
	public UsuarioDTO salvar(UsuarioSalvarDTO usuarioDTO) {
		
		var optUsuarioEmail = this.repository.findByEmail(usuarioDTO.getEmail());
		
		if(optUsuarioEmail.isPresent()) throw new IllegalParameterException("Erro! email já está em uso!");
		
		var usuario = new Usuario(usuarioDTO);
		usuario.setSenha(this.bCrypt.encode(usuarioDTO.getSenha()));
		usuario.addTipo(TipoUsuario.USUARIO.getCodigo());
		
		usuario = this.repository.save(usuario);
		
		return new UsuarioDTO(usuario);
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public UsuarioDTO atualizar(Long id, UsuarioAtualizarDTO usuarioDTO) {
		
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
		
		if(!usuarioDTO.getSenha().isEmpty()) {
			
			var senha = usuarioDTO.getSenha();
			
			if(senha.length() < 8 || senha.length() > 15)
				throw new IllegalParameterException("Erro! O campo senha deve ter entre 8 a 15 caracteres");
			
			usuario.setSenha(bCrypt.encode(usuarioDTO.getSenha()));
			
		}
		
		usuario = this.repository.save(usuario);
		
		return new UsuarioDTO(usuario);
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void excluir(Long id) {
		
		var optUsuario = this.repository.findById(id);
		
		//Verifica se usuario é o mesmo que está logado
		this.restricaoService.verificarUsuario(id);
		
		if(!optUsuario.isPresent())
			throw new ObjectNotFoundFromParameterException(
					"Erro! usuário não encontrado para o id informado!");
		
		this.repository.delete(optUsuario.get());
		
	}
	
	public UsuarioDTO buscarPorId(Long id) {
		
		var optUsuario =  this.repository.findById(id);
		
		if(!optUsuario.isPresent()) 
			throw new ObjectNotFoundFromParameterException("Erro! usuario não encontrado para id informado!");
		
		//Verifica se usuario é o mesmo que está logado
		this.restricaoService.verificarUsuario(optUsuario.get().getId());
		
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
	
	public Page<UsuarioDTO> listar(Pageable paginacao){
		
		var pageUsuarios = this.repository.findAll(paginacao);
		
		return pageUsuarios.map(UsuarioDTO::new);
		
	}
	
}
