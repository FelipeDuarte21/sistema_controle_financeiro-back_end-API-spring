package br.com.felipeduarte.APIControleFinanceiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;

@Repository
public interface BalancoRepository extends JpaRepository<Balanco, Long> {
	
	public Balanco findByCategoriaAndFechado(Categoria categoria,Boolean fechado);
	public Balanco findByCategoriaAndMesAndAno(Categoria categoria, Integer mes, Integer ano);
	
}
