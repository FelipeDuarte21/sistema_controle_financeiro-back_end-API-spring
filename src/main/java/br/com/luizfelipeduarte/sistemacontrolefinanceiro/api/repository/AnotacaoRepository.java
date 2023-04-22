package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Anotacao;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Categoria;

@Repository
public interface AnotacaoRepository extends JpaRepository<Anotacao, Long>{
	
	public List<Anotacao> findByCategoriaOrderByDataDesc(Categoria categoria);
	
}