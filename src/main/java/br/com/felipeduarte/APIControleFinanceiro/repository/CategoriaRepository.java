package br.com.felipeduarte.APIControleFinanceiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.Usuario;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	public Optional<Categoria> findByNomeAndUsuario(String nome,Usuario usuario);
	public Page<Categoria> findByUsuario(Usuario usuario, Pageable pageable);
	public List<Categoria> findByUsuario(Usuario categoria);
	
}
