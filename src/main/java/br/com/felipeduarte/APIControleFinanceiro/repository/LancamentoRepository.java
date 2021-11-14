package br.com.felipeduarte.APIControleFinanceiro.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
	
	public Page<Lancamento> findByBalanco(Balanco balanco,Pageable pageable);
	public List<Lancamento> findByBalanco(Balanco balanco);
	
}
