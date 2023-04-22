package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Categoria;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.LancamentoFixo;

@Repository
public interface LancamentoFixoRepository extends JpaRepository<LancamentoFixo, Long>{
	
	public List<LancamentoFixo> findByCategoria(Categoria categoria);
	public Optional<LancamentoFixo> findByDescricao(String descricao);

}
