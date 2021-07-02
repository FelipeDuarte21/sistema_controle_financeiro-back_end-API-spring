package br.com.felipeduarte.APIControleFinanceiro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Categoria;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.CategoriaDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	public Categoria salvar(CategoriaDTO categoria) {
		return null;
	}
	
	public Categoria atualizar(CategoriaDTO  categoria) {
		return null;
	}
	
	public boolean excluir(Integer id) {
		return false;
	}
	
	public Page<Categoria> listar(Integer page, Integer size, Integer order){
		return null;
	}
	
}
