package br.com.felipeduarte.APIControleFinanceiro.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.repository.BalancoRepository;

@Service
public class BalancoService {
	
	@Autowired
	private BalancoRepository repository;
	
	@Autowired
	private CategoriaService categoriaService;
	
	public Balanco recuperarAtual(Long idCategoria) {
		
		Categoria categoria = this.categoriaService.buscarPorId(idCategoria);
		
		if(categoria == null) {
			return null;
		}
		
		Balanco balanco = this.repository.findByCategoriaAndFechado(categoria, false);
		
		return balanco;
	}
	
	public Balanco recuperarPorData(Long idCategoria, Integer mes, Integer ano) {
		
		Categoria categoria = this.categoriaService.buscarPorId(idCategoria);
		
		if(categoria == null) {
			return null;
		}
		
		Balanco balanco = this.repository.findByCategoriaAndMesAndAno(categoria, mes, ano);
		
		return balanco;
		
	}
	
	public Balanco buscarPorId(Long id) {
		
		Optional<Balanco> balanco = this.repository.findById(id);
		
		if(balanco.isEmpty()) {
			return null;
		}
		
		return balanco.get();
	}
	
}
