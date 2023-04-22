package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Usuario;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.UsuarioDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.UsuarioDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.enums.TipoUsuario;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository.UsuarioRepository;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.IllegalParameterException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;

@Service
public class UsuarioService {
	
	private UsuarioRepository repository;
	
	private BCryptPasswordEncoder bCrypt;
	
	private ContaService contaService;
	
	private CategoriaService categoriaService;
	
	@Autowired
	public UsuarioService(UsuarioRepository repository, BCryptPasswordEncoder bCrypt,
			ContaService contaService,CategoriaService categoriaService) {
		this.repository = repository;
		this.bCrypt = bCrypt;
		/*this.restricaoService = restricaoService;*/
		this.contaService = contaService;
		this.categoriaService = categoriaService;
	}

	@Transactional(rollbackOn = Exception.class)
	public UsuarioDTO cadastrar(UsuarioDadosDTO usuarioDadosDTO) {
		
		var optUsuarioEmail = this.repository.findByEmail(usuarioDadosDTO.getEmail());
		
		if(optUsuarioEmail.isPresent()) throw new IllegalParameterException("Erro! email já está em uso!");
		
		var usuario = new Usuario(usuarioDadosDTO);
		usuario.setSenha(this.bCrypt.encode(usuarioDadosDTO.getSenha()));
		usuario.getTipo().add(TipoUsuario.USUARIO.getCodigo());
		
		usuario = this.repository.save(usuario);
		
		var conta = this.contaService.cadastrar(usuario,usuarioDadosDTO.getRendaMensalTotal());
		
		usuario.setConta(conta);
		
		this.categoriaService.cadastrarCategoriasConta(conta, usuarioDadosDTO.getCategorias());
		
		return new UsuarioDTO(usuario);
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public UsuarioDTO atualizar(Long id, UsuarioDadosDTO usuarioDTO) {
		
		if(id == null) throw new IllegalParameterException("Erro! id não pode ser nullo");
		if(id == 0) throw new IllegalParameterException("Erro! id não pode ser 0");
		
		var optUsuario = this.repository.findById(id);
		
		if(!optUsuario.isPresent())
			throw new ObjectNotFoundFromParameterException(
					"Erro! usuário não encontrado para o id informado!");
		
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

		if(!optUsuario.isPresent())
			throw new ObjectNotFoundFromParameterException(
					"Erro! usuário não encontrado para o id informado!");
		
		this.contaService.excluir(optUsuario.get().getConta());
		
		this.repository.delete(optUsuario.get());
		
	}
	
	public UsuarioDTO buscarPorId(Long id) {
		
		var optUsuario =  this.repository.findById(id);
		
		if(!optUsuario.isPresent()) 
			throw new ObjectNotFoundFromParameterException("Erro! usuario não encontrado para id informado!");
		
		return new UsuarioDTO(optUsuario.get());
		
	}
	
}
