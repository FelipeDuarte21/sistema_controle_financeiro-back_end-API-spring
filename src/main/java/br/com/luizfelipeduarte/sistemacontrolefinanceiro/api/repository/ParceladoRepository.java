package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Categoria;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Parcelado;

@Repository
public interface ParceladoRepository extends JpaRepository<Parcelado, Long> {
	
	public List<Parcelado> findByCategoriaOrderByDataRegistroDesc(Categoria categoria);
	public List<Parcelado> findByCategoriaAndQuitado(Categoria categoria,Boolean quitado);
	
}
