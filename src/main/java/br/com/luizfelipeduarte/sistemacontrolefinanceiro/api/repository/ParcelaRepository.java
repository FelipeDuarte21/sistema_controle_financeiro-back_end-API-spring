package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Parcela;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Parcelado;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long>{
	
	public List<Parcela> findByParceladoOrderByNumero(Parcelado parcelado);
	
	public List<Parcela> findByParceladoAndPago(Parcelado parcelado,Boolean pago);
	
}
