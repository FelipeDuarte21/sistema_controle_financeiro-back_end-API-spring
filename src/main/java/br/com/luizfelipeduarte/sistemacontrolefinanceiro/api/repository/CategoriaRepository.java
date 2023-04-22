package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Categoria;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Conta;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	public List<Categoria> findByConta(Conta conta);
	
}
