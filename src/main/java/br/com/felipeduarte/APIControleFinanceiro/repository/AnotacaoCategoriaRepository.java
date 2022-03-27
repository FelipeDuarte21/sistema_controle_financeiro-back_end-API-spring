package br.com.felipeduarte.APIControleFinanceiro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.felipeduarte.APIControleFinanceiro.model.AnotacaoCategoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;

@Repository
public interface AnotacaoCategoriaRepository extends JpaRepository<AnotacaoCategoria, Long>{
	
	public Page<AnotacaoCategoria> findByCategoria(Categoria categoria, Pageable pageable);

}