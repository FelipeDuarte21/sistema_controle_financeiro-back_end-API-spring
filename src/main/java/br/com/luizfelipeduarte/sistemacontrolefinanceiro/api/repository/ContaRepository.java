package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Conta;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Usuario;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
	
	public Conta findByUsuario(Usuario usuario);

}
