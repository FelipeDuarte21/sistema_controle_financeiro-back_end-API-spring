package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Categoria;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Folha;

@Repository
public interface FolhaRepository extends JpaRepository<Folha, Long> {
	
	public List<Folha> findByCategoriaOrderByMesAnoDesc(Categoria categoria);
	public Optional<Folha> findByCategoriaAndMesAno(Categoria categoria,YearMonth mesAno);
	
}
