package br.com.felipeduarte.APIControleFinanceiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.felipeduarte.APIControleFinanceiro.model.Parcela;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long>{

}
