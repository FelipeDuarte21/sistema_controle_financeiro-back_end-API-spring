package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Parcela;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Parcelado;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.ParcelaDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.ParcelaDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.ParcelaPagamentoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository.ParcelaRepository;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.ObjectNotFoundFromParameterException;

@Service
public class ParcelaService {
	
	private ParcelaRepository repository;
	
	@Autowired
	public ParcelaService(ParcelaRepository repository) {
		this.repository = repository;
	}
	
	public void cadastrar(Parcelado parcelado, ParcelaDadosDTO parcelaDadosDTO) {
		
		for(int i=0; i < parcelado.getTotalParcelas(); i++) {
			
			Parcela parcela = new Parcela();
			
			parcela.setNumero(i+1);
			parcela.setValor(parcelaDadosDTO.getValor());
			parcela.setPago(false);
			parcela.setDataVencimento(parcelaDadosDTO.getDataVencimentoPrimeiraParcela().plusMonths(i));
			
			parcela.setParcelado(parcelado);
			
			this.repository.save(parcela);
			
 		}
		
	}
	
	public void alterar(Parcelado parcelado, ParcelaDadosDTO parcelaDadosDTO) {
		
		for(int i=0; i < parcelado.getTotalParcelas(); i++) {
			
			Parcela parcela = parcelado.getParcelas().get(i);
			parcela.setDataVencimento(parcelaDadosDTO.getDataVencimentoPrimeiraParcela().plusMonths(i));
			parcela.setValor(parcelaDadosDTO.getValor());
			
			this.repository.save(parcela);
			
		}
		
	}
	
	public void excluir(Parcelado parcelado) {
		
		parcelado.getParcelas().forEach(parcela -> {
			this.repository.delete(parcela);
		});
		
	}
	
	public List<ParcelaDTO> listarParcelas(Parcelado Parcelado){
		
		var parcelados = this.repository.findByParceladoOrderByNumero(Parcelado);
		
		return parcelados.stream().map(ParcelaDTO::new).collect(Collectors.toList());
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public ParcelaDTO pagarParcela(Long idParcela, ParcelaPagamentoDTO parcelaPagamentoDTO) {
		
		var optParcela = this.repository.findById(idParcela);
		
		if(!optParcela.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! parcela não encontrada para id informado!");
		
		/*
		//Verifica permissão
		this.restricaoService.verificarPermissaoConteudo(optParcela.get().getParcelado().getCategoria());*/
		
		var parcela = optParcela.get();
		
		parcela.setValor(parcelaPagamentoDTO.getValor());
		parcela.setDataVencimento(parcelaPagamentoDTO.getDataVencimento());
		parcela.setDataPagamento(parcelaPagamentoDTO.getDataPagamento());
		parcela.setPago(true);
		this.repository.save(parcela);
		
		return new ParcelaDTO(parcela);
		
	}
	
	public List<ParcelaDTO> listarParcelasNaoPagas(Parcelado parcelado){
		
		var parcelas = this.repository.findByParceladoAndPago(parcelado, false);
		
		return parcelas.stream().map(ParcelaDTO::new).collect(Collectors.toList());
		
	}
	
}
