package br.com.felipeduarte.APIControleFinanceiro.repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;

@Repository
public interface BalancoRepository extends JpaRepository<Balanco, Long> {
	
	public Optional<Balanco> findByCategoriaAndMesAno(Categoria categoria, YearMonth mesAno);
	public List<Balanco> findByCategoriaAndMesAnoBetween(YearMonth de, YearMonth ate);
	
}
