package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.email.EmailService;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.EmailDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.UsuarioRepository;
import br.com.felipeduarte.APIControleFinanceiro.security.JWTUtil;
import br.com.felipeduarte.APIControleFinanceiro.security.UsuarioDetalhe;
import br.com.felipeduarte.APIControleFinanceiro.security.UsuarioDetalheService;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;

@Service
public class AuthService {
	
	private UsuarioDetalheService usuarioDetalheService;
	private JWTUtil jwtUtil;
	private UsuarioRepository usuarioRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private EmailService emailService;
	
	private Random rand = new Random();
	
	@Autowired
	public AuthService(UsuarioDetalheService usuarioDetalheService, JWTUtil jwtUtil,
			UsuarioRepository usuarioRepository,BCryptPasswordEncoder bCryptPasswordEncoder,
			EmailService emailService) {
		this.usuarioDetalheService = usuarioDetalheService;
		this.jwtUtil = jwtUtil;
		this.usuarioRepository = usuarioRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.emailService = emailService;
	}
	
	public String refreshToken() throws Exception {
		Optional<UsuarioDetalhe> optUsuario = this.usuarioDetalheService.getUsuarioAutenticado();
		String token = this.jwtUtil.geradorToken(optUsuario.get());
		return token;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void resetSenha(EmailDTO email) {
		
		var optUsuario = this.usuarioRepository.findByEmail(email.getEmail());
		
		if(!optUsuario.isPresent())
			throw new IllegalParameterException("Erro! usuário não encontrado para email informado!");
		
		var usuario = optUsuario.get();
		
		String senhaNova = gerarNovaSenha();
		
		usuario.setSenha(this.bCryptPasswordEncoder.encode(senhaNova));
		
		this.usuarioRepository.save(usuario);
		
		this.emailService.enviarEmailNovaSenha(usuario, senhaNova);
		
	}
	
	private String gerarNovaSenha() {
		char[] vet = new char[10];
		
		for(int i=0; i < 10; i++) {
			vet[i] = randomChar();
		}
		
		return new String(vet);
		
	}
	
	private char randomChar() {
		int opt = rand.nextInt(3);
		
		if(opt == 0) { //digito
			return (char) (rand.nextInt(10) + 48);
			
		}else if(opt == 1) { //Letra Maiúscula
			return (char) (rand.nextInt(26) + 65);
			
		}else { //Letra minuscula
			return (char) (rand.nextInt(26) + 97);
			
		}
		
	}

}
