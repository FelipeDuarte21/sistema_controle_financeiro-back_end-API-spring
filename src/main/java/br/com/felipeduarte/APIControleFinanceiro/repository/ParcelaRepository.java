package br.com.felipeduarte.APIControleFinanceiro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.felipeduarte.APIControleFinanceiro.model.Parcela;
import br.com.felipeduarte.APIControleFinanceiro.model.Parcelado;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long>{
	
	public Page<Parcela> findByParcelado(Parcelado parcelado, Pageable pageable);

}
