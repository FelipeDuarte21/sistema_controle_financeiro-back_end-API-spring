package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Conta;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Usuario;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository.ContaRepository;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.IllegalParameterException;

@Service
public class ContaService {

	private ContaRepository repository;
	
	@Autowired
	public ContaService(ContaRepository repository) {
		this.repository = repository;
	}
	
	public Conta cadastrar(Usuario usuario, BigDecimal rendaMensalInicial) {
		
		var conta = new Conta();
		conta.setRendaMensalTotal(rendaMensalInicial);
		conta.setUsuario(usuario);
		
		return this.repository.save(conta);
		
	}
	
	public void excluir(Conta conta) {
		
		this.repository.delete(conta);
		
	}
	
	public Conta buscarPorId(Long id) {
		
		Optional<Conta> optConta = this.repository.findById(id);
		
		if(optConta.isEmpty()) throw new IllegalParameterException("Erro! conta não econtrada!!");
		
		return optConta.get();
		
	}

	public void atualizarRendaMensal(Long id, BigDecimal rendaMensal){

		Optional<Conta> optConta = this.repository.findById(id);

		if(optConta.isEmpty()) throw new IllegalParameterException("Erro! conta não encontrada!!");

		Conta conta = optConta.get();
		conta.setRendaMensalTotal(rendaMensal);

		this.repository.save(conta);

	}
	
}
