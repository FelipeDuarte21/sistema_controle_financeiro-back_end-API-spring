package br.com.felipeduarte.APIControleFinanceiro.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Parcela;
import br.com.felipeduarte.APIControleFinanceiro.model.Parcelado;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.ParcelaDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.ParcelaPagarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.ParceladoDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.ParceladoSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.enums.TipoLancamentoEnum;
import br.com.felipeduarte.APIControleFinanceiro.repository.ParcelaRepository;
import br.com.felipeduarte.APIControleFinanceiro.repository.ParceladoRepository;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

@Service
public class ParceladoService {
	
	private Clock clock;
	
	private ParceladoRepository repository;
	
	private CategoriaService categoriaService;
	
	private RestricaoService restricaoService;
	
	private ParcelaRepository parcelaRepository;
	
	private BalancoService balancoService;
	
	private LancamentoService lancamentoService;
	
	@Autowired
	public ParceladoService(Clock clock, ParceladoRepository parceladoRepository,
			CategoriaService categoriaService, RestricaoService restricaoService,
			ParcelaRepository parcelaRepository ,BalancoService balancoService, 
			LancamentoService lancamentoService) {
		this.clock = clock;
		this.repository = parceladoRepository;
		this.categoriaService = categoriaService;
		this.restricaoService = restricaoService;
		this.parcelaRepository = parcelaRepository;
		this.balancoService = balancoService;
		this.lancamentoService = lancamentoService;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public ParceladoDTO cadastrar(Long idCategoria, ParceladoSalvarDTO parceladoDTO) {
		
		var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
		
		var parcelado = new Parcelado(parceladoDTO);
		parcelado.setCategoria(categoria);
		parcelado.setDataRegistro(LocalDateTime.now(clock));
		
		for(int i=0; i < parceladoDTO.getTotalParcela(); i++) {
			var parcela = new Parcela();
			parcela.setNumero(i+1);
			parcela.setValor(parceladoDTO.getValor());
			parcela.setPago(false);
			parcela.setDataVencimento(parceladoDTO.getDataVencimentoPrimeiraParcela().plusMonths(i));
			if(i == 0) parcela.setPrimeiro(true);
			else parcela.setPrimeiro(false);
			if((parceladoDTO.getTotalParcela() - i) == 1) parcela.setUltimo(true);
			else parcela.setUltimo(false);
			parcela.setParcelado(parcelado);
			parcelado.addParcela(parcela);
 		}
		
		parcelado = this.repository.save(parcelado);
		
		return new ParceladoDTO(parcelado);
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public ParceladoDTO alterar(Long id, ParceladoSalvarDTO parceladoDTO) {
		
		var optParcelado = this.repository.findById(id);
		
		if(!optParcelado.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! parcelado não encontrado para o id informado!");
		
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optParcelado.get().getCategoria());
		
		var parcelado = optParcelado.get();
		parcelado.setTitulo(parceladoDTO.getTitulo());
		parcelado.setDescricao(parceladoDTO.getDescricao());
		parcelado.setData(parceladoDTO.getData());
		
		for(int i=0; i < parcelado.getParcelas().size(); i++) {
			var parcela = parcelado.getParcelas().get(i);
			parcela.setDataVencimento(parceladoDTO.getDataVencimentoPrimeiraParcela().plusMonths(i));
			parcela.setValor(parceladoDTO.getValor());
		}
	
		parcelado = this.repository.save(parcelado);
		
		return new ParceladoDTO(parcelado);
		
	}
	
	public void excluir(Long id) {
		
		var optParcelado = this.repository.findById(id);
		
		if(!optParcelado.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! parcelado não encontrado para o id informado!");
		
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optParcelado.get().getCategoria());
		
		this.repository.delete(optParcelado.get());
		
	}
	
	public ParceladoDTO buscarPorId(Long id) {
		
		var optParcelado = this.repository.findById(id);
		
		if(!optParcelado.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! parcelado não encontrado para o id informado!");
		
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optParcelado.get().getCategoria());
		
		return new ParceladoDTO(optParcelado.get());
		
	}
	
	public Page<ParceladoDTO> listar(Long idCategoria, Integer page, Integer size, Integer order){
		
		if(page < 0) 
			throw new IllegalParameterException("Erro! o número da página não pode ser negativo!");
		
		if(size < 1) 
			throw new IllegalParameterException("Erro! a quantidade de elementos na página é no mínimo 1");
		
		var direction = Direction.ASC;
		
		if(order == 2) direction = Direction.DESC;
		
		var pageable = PageRequest.of(page, size,direction,"dataRegistro");
		
		var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
		
		var pageParcelados = this.repository.findByCategoria(categoria, pageable);
		
		var pageParceladosDTO = new PageImpl<ParceladoDTO>(
				pageParcelados.getContent().stream().map(ParceladoDTO::new).collect(Collectors.toList()),
					pageParcelados.getPageable(),pageParcelados.getTotalElements());
		
		return pageParceladosDTO;
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public ParcelaDTO pagarParcela(Long idCategoria, Long idParcela, ParcelaPagarDTO parcelaPagarDTO) {
		
		var optParcela = this.parcelaRepository.findById(idParcela);
		
		if(!optParcela.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! parcela não encontrada para id informado!");
		
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optParcela.get().getParcelado().getCategoria());
		
		var parcela = optParcela.get();
		
		parcela.setValor(parcelaPagarDTO.getValor());
		parcela.setDataVencimento(parcelaPagarDTO.getDataVencimento());
		parcela.setDataPagamento(parcelaPagarDTO.getDataPagamento());
		parcela.setPago(true);
		this.parcelaRepository.save(parcela);
		
		var parcelado = parcela.getParcelado();
		
		var lancamento = new LancamentoSalvarDTO();
		lancamento.setNome(parcelado.getTitulo());
		lancamento.setDescricao(
				String.format("Parcela %d de %d", 
						parcela.getNumero(), parcelado.getParcelas().size()));
		lancamento.setData(parcela.getDataPagamento());
		lancamento.setValor(parcela.getValor());
		lancamento.setTipo(TipoLancamentoEnum.DESPESA.getValor());
		var balanco  = this.balancoService.recuperarAtual(idCategoria);
		this.lancamentoService.salvar(balanco.getId(), lancamento);
		
		for(int i=0; i < parcelado.getParcelas().size(); i++) {
			
			var p = parcelado.getParcelas().get(i);
			
			if(!p.getPago()) break;
			
			if(p.getUltimo()) {
				parcelado.setQuitado(true);
				this.repository.save(parcelado);
			}
			
		}
		
		return new ParcelaDTO(parcela);
		
	}
	
	public Page<ParcelaDTO> listarParcelas(Long idCategoria, Long idParcelado, 
			Integer page, Integer size, Integer order){
		
		var optParcelado = this.repository.findById(idParcelado);
		
		if(!optParcelado.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! Parcelado não encontrado para o id informado!");
		
		if(page < 0) 
			throw new IllegalParameterException("Erro! o número da página não pode ser negativo!");
		
		if(size < 1) 
			throw new IllegalParameterException("Erro! a quantidade de elementos na página é no mínimo 1");
		
		var direction = Direction.ASC;
		
		if(order == 2) direction = Direction.DESC;
		
		var pageable = PageRequest.of(page, size,direction,"dataVencimento");
		
		//verificar permissão de conteudo
		this.restricaoService.verificarPermissaoConteudo(optParcelado.get().getCategoria());
		
		var pagParcelas = this.parcelaRepository.findByParcelado(optParcelado.get(), pageable);
		
		return pagParcelas.map(ParcelaDTO::new);
		
	}

}
