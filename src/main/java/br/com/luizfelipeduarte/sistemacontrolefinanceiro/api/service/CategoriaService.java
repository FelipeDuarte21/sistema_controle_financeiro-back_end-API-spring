package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Categoria;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Conta;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.CategoriaDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.CategoriaDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.CategoriaPersonalizadaDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.CategoriaPorcentagemDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository.CategoriaRepository;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.IllegalParameterException;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;

@Service
public class CategoriaService {
	
	private Clock clock;
	
	private CategoriaRepository repository;
	
	private ContaService contaService;
	
	/*private RestricaoService restricaoService;*/
	
	@Autowired
	public CategoriaService(CategoriaRepository repository, Clock clock/*, RestricaoService restricaoService*/
			,ContaService contaService) {
		this.repository = repository;
		this.clock = clock;
		//this.restricaoService = restricaoService;
		this.contaService = contaService;
	}

	@Transactional(rollbackOn = Exception.class)
	public CategoriaDTO salvar(CategoriaDadosDTO categoriaDadosDTO) {
		
		/*//Obtem o usuário logado
		Usuario usuario = this.restricaoService.getUsuario();*/
		
		Conta conta = this.contaService.buscarPorId(categoriaDadosDTO.getConta());
		
		Categoria categoria = new Categoria(categoriaDadosDTO);
		
		categoria.setConta(conta);
		categoria.setDataCadastro(LocalDate.now(clock.getZone()));
		
		categoria = this.repository.save(categoria);
		
		return new CategoriaDTO(categoria);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void cadastrarCategoriasConta(Conta conta, List<CategoriaPersonalizadaDTO> categorias) {
		
		LocalDate hoje = LocalDate.now(clock);
		
		if(categorias != null && !categorias.isEmpty()) {
			
			if(categorias.stream().mapToDouble(c -> c.getPorcentagem()).sum() != 100.0) {
				throw new IllegalParameterException("Erro! Porcentagem não é igual a 100%");
			}
			
			categorias.forEach(categoria -> {
				
				var cat = new Categoria(categoria.getNome(),categoria.getDescricao(),
						hoje,categoria.getPorcentagem(),conta);
				
				this.repository.save(cat);
				
			});
			
			
		}else { //PADRÃO DO SISTEMA
			
			var cat1 = new Categoria("Despesa Gerais",
					"Categoria separada para despesas do mês - PADRÃO DO SISTEMA",hoje,50.0,conta);
		
			var cat2 = new Categoria("Fundo de Emergência",
					"Categoria separada para fundo de emergência - PADRÃO DO SISTEMA",hoje,40.0,conta);
			
			var cat3 = new Categoria("Livre",
					"Categoria separada para o dinheiro livre - PADRÃO DO SISTEMA",hoje,10.0,conta);
			
			this.repository.saveAll(Arrays.asList(cat1,cat2,cat3));
			
		}
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public CategoriaDTO atualizar(Long id, CategoriaDadosDTO categoriaDTO) {
		
		if(id == null) throw new IllegalParameterException("Erro! id não pode ser nullo");
		if(id == 0) throw new IllegalParameterException("Erro! id não pode ser 0");
		 
		Optional<Categoria> optCategoria = this.repository.findById(id);
		
		if(!optCategoria.isPresent())
			throw new ObjectNotFoundFromParameterException(
					"Erro! categoria não econtrada para o id informado!");
		/*
		//Verifica se categoria pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(optCategoria.get());*/
		
		Categoria categoria = optCategoria.get();
		categoria.setNome(categoriaDTO.getNome());
		categoria.setDescricao(categoriaDTO.getDescricao());
		
		categoria = this.repository.save(categoria);
		
		return new CategoriaDTO(categoria);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void excluir(Long id) {
		
		Optional<Categoria> optCategoria = this.repository.findById(id);
		
		if(!optCategoria.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! categoria não econtrada para o id informado!");
		
		/*
		//Verifica se categoria pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(optCategoria.get());*/
		
		this.repository.delete(optCategoria.get());
		
	}
	
	public CategoriaDTO buscarPorId(Long id) {
		Categoria categoria = buscarPorIdInterno(id);
		return new CategoriaDTO(categoria);
	}
	
	public Categoria buscarPorIdInterno(Long id) {
		
		var optCategoria = this.repository.findById(id);
		
		if(!optCategoria.isPresent()) 
			throw new IllegalParameterException(
					"Erro! categoria não econtrada para o id informado!");
		/*
		//Verifica se categoria pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(optCategoria.get());*/
		
		return optCategoria.get();
	}
	
	public List<CategoriaDTO> listar(Long idConta) {
		
		Conta conta = this.contaService.buscarPorId(idConta);
		
		List<Categoria> categorias = this.repository.findByConta(conta);
		
		return categorias.stream().map(CategoriaDTO::new).collect(Collectors.toList());
		
	}

	@Transactional(rollbackOn = Exception.class)
	public List<CategoriaDTO> atualizarPorcentagem(List<CategoriaPorcentagemDTO> categoriasPorcentagemDTO) {
		
		if(categoriasPorcentagemDTO.stream().mapToDouble(c -> c.getPorcentagem()).sum() != 100.0) {
			throw new IllegalParameterException("Erro! Porcentagem não é igual a 100%");
		}
		
		var categorias = new ArrayList<Categoria>();
		
		categoriasPorcentagemDTO.forEach(categoriaPorcentagemDTO -> {
			
			
			var categoria = buscarPorIdInterno(categoriaPorcentagemDTO.getId());
			
			categoria.setPorcentagem(categoriaPorcentagemDTO.getPorcentagem());
			
			categoria = this.repository.save(categoria);
			
			categorias.add(categoria);
			
			
		});
		
		return categorias.stream().map(CategoriaDTO::new).collect(Collectors.toList());
		
	}
	
	
}
