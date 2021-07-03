package br.com.felipeduarte.APIControleFinanceiro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.felipeduarte.APIControleFinanceiro.model.TipoLancamento;

@Repository
public interface TipoLancamentoRepository extends JpaRepository<TipoLancamento, Long> {
	
	public Optional<TipoLancamento> findByValor(Integer valor);
	
}
