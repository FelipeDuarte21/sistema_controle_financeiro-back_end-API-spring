package br.com.felipeduarte.APIControleFinanceiro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.LancamentoSalvo;

@Repository
public interface LancamentoSalvoRepository extends JpaRepository<LancamentoSalvo, Long>{
	
	public Page<LancamentoSalvo> findByCategoria(Categoria categoria, Pageable pageable);

}
