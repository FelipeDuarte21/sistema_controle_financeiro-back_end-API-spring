package br.com.felipeduarte.APIControleFinanceiro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Parcelado;

@Repository
public interface ParceladoRepository extends JpaRepository<Parcelado, Long> {
	
	public Page<Parcelado> findByCategoria(Categoria categoria,Pageable pageable);

}
