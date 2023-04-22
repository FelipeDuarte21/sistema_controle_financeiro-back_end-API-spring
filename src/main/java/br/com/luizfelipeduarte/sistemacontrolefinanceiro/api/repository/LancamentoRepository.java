package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Folha;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Lancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
	
	public List<Lancamento> findByFolhaOrderByDataRegistroDesc(Folha folha);
	public List<Lancamento> findByFolha_IdOrderByDataRegistroDesc(Long id);
	
}
