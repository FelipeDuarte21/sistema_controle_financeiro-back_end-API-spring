package br.com.felipeduarte.APIControleFinanceiro.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;

@Repository
public interface BalancoRepository extends JpaRepository<Balanco, Long> {
	
	public List<Balanco> findByCategoria(Categoria categoria, Sort ordem);
	public Balanco findByCategoriaAndFechado(Categoria categoria,Boolean fechado);
	public Balanco findByCategoriaAndMesAndAno(Categoria categoria, Integer mes, Integer ano);
	public List<Balanco> findByFechadoAndMesAndAno(Boolean fechado,Integer mes,Integer ano);
	
}
