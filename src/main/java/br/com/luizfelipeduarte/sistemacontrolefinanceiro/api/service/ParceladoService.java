package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Categoria;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Parcelado;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.ParcelaDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.ParceladoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.ParceladoDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository.ParceladoRepository;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;

@Service
public class ParceladoService {
	
	private Clock clock;
	
	private ParceladoRepository repository;
	
	private CategoriaService categoriaService;
	
	/*private RestricaoService restricaoService;*/
	
	private ParcelaService parcelaService;
	
	@Autowired
	public ParceladoService(Clock clock, ParceladoRepository parceladoRepository,
			CategoriaService categoriaService/*, RestricaoService restricaoService*/,
			ParcelaService parcelaService) {
		this.clock = clock;
		this.repository = parceladoRepository;
		this.categoriaService = categoriaService;
		//this.restricaoService = restricaoService;
		this.parcelaService = parcelaService;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public ParceladoDTO cadastrar(Long idCategoria, ParceladoDadosDTO parceladoDadosDTO) {
		
		Categoria categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
		
		Parcelado parcelado = new Parcelado(parceladoDadosDTO);
		parcelado.setCategoria(categoria);
		parcelado.setDataRegistro(LocalDateTime.now(clock));
		parcelado.setValorTotal(parceladoDadosDTO.getParcelaDados().getValor().multiply(new BigDecimal(parceladoDadosDTO.getTotalParcela())));
		
		parcelado = this.repository.save(parcelado);
		
		this.parcelaService.cadastrar(parcelado, parceladoDadosDTO.getParcelaDados());
		
		return new ParceladoDTO(parcelado);
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public ParceladoDTO alterar(Long id, ParceladoDadosDTO parceladoDadosDTO) {
		
		var optParcelado = this.repository.findById(id);
		
		if(!optParcelado.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! parcelado não encontrado para o id informado!");
		/*
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optParcelado.get().getCategoria());*/
		
		var parcelado = optParcelado.get();
		parcelado.setTitulo(parceladoDadosDTO.getTitulo());
		parcelado.setDescricao(parceladoDadosDTO.getDescricao());
		parcelado.setData(parceladoDadosDTO.getData());
		parcelado.setValorTotal(parceladoDadosDTO.getParcelaDados().getValor().multiply(new BigDecimal(parceladoDadosDTO.getTotalParcela())));
		
		this.parcelaService.alterar(parcelado, parceladoDadosDTO.getParcelaDados());
	
		parcelado = this.repository.save(parcelado);
		
		return new ParceladoDTO(parcelado);
		
	}
	
	public void excluir(Long id) {
		
		var optParcelado = this.repository.findById(id);
		
		if(!optParcelado.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! parcelado não encontrado para o id informado!");
		/*
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optParcelado.get().getCategoria());*/
		
		this.parcelaService.excluir(optParcelado.get());
		
		this.repository.delete(optParcelado.get());
		
	}
	
	public ParceladoDTO buscarPorId(Long id) {
		
		var optParcelado = this.repository.findById(id);
		
		if(!optParcelado.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! parcelado não encontrado para o id informado!");
		/*
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optParcelado.get().getCategoria());*/
		
		return new ParceladoDTO(optParcelado.get());
		
	}
	
	public List<ParceladoDTO> listar(Long idCategoria){
		
		var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
		
		var parcelados = this.repository.findByCategoriaOrderByDataRegistroDesc(categoria);
		
		return parcelados.stream().map(ParceladoDTO::new).collect(Collectors.toList());
		
	}
	
	public List<ParceladoDTO> listarNaoQuitados(Long idCategoria){
		
		var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
		
		var parcelados = this.repository.findByCategoriaAndQuitado(categoria, false);
		
		return parcelados.stream().map(ParceladoDTO::new).collect(Collectors.toList());
		
	}
	
	public ParceladoDTO atualizarParcelado(Long idParcelado) {
		
		var optParcelado = this.repository.findById(idParcelado);
		
		if(!optParcelado.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! Parcelado não encontrado para o o id informado!");
		
		var parcelado = optParcelado.get();
		
		Double valorPago = parcelado.getParcelas().stream().filter(p -> p.getPago())
				.mapToDouble(p -> p.getValor().doubleValue()).sum();
		
		Long totalParcelasPagas = parcelado.getParcelas().stream().filter(p -> p.getPago()).count();
		
		Boolean quitado = parcelado.getParcelas().stream().filter(p -> p.getPago()).count() == parcelado.getTotalParcelas();
		
		parcelado.setValorPago(new BigDecimal(valorPago));
		parcelado.setTotalParcelasPagas(totalParcelasPagas.intValue());
		parcelado.setQuitado(quitado);
		
		this.repository.save(parcelado);
		
		return new ParceladoDTO(parcelado);
		
	}
	
	public List<ParcelaDTO> listarParcelas(Long idParcelado){
		
		var optParcelado = this.repository.findById(idParcelado);
		
		if(!optParcelado.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! Parcelado não encontrado para o id informado!");
		/*
		//verificar permissão de conteudo
		this.restricaoService.verificarPermissaoConteudo(optParcelado.get().getCategoria());*/
		
		return this.parcelaService.listarParcelas(optParcelado.get());
		
	}
	
	public List<ParcelaDTO> listarParcelasNaoPagas(Long idParcelado){
		
		var optParcelado = this.repository.findById(idParcelado);
		
		if(!optParcelado.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! Parcelado não encontrado para o id informado!");
		/*
		//verificar permissão de conteudo
		this.restricaoService.verificarPermissaoConteudo(optParcelado.get().getCategoria());*/
		
		return this.parcelaService.listarParcelasNaoPagas(optParcelado.get());
		
	}
	
}
