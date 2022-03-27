package br.com.felipeduarte.APIControleFinanceiro.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.ArquivoDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoSalvoSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.TransferenciaDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.enums.TipoLancamentoEnum;
import br.com.felipeduarte.APIControleFinanceiro.repository.LancamentoRepository;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;

@Service
public class LancamentoService {
	
	private Clock clock;
	
	private LancamentoRepository repository;
	
	private BalancoService balancoService;
	
	private TipoLancamentoService tipoLancamentoService;
	
	private RestricaoService restricaoService;
	
	private LancamentoSalvoService lancamentoSalvoService;

	
	@Autowired
	public LancamentoService(Clock clock, LancamentoRepository repository, BalancoService balancoService,
			TipoLancamentoService tipoLancamentoService, RestricaoService restricaoService,
			LancamentoSalvoService lancamentoSalvoService) {
		this.clock = clock;
		this.repository = repository;
		this.balancoService = balancoService;
		this.tipoLancamentoService = tipoLancamentoService;
		this.restricaoService = restricaoService;
		this.lancamentoSalvoService = lancamentoSalvoService;
	}

	@Transactional(rollbackOn = Exception.class)
	public LancamentoDTO salvar(Long idBalanco, LancamentoSalvarDTO lancamentoDTO) {
		
		var balanco = this.balancoService.buscarPorId(idBalanco);
		
		//Verifica se o balanço pertence ao usuario
		this.restricaoService.verificarPermissaoConteudo(balanco.getCategoria());
		
		if(balanco.getFechado()) 
			throw new IllegalParameterException(
					"Erro! o balanço do lançamento já está fechado!");
		
		var tipoLancamento = 
				this.tipoLancamentoService.buscarPorValor(lancamentoDTO.getTipo());
	
		var lancamento = new Lancamento(lancamentoDTO);
		lancamento.setTipo(tipoLancamento);
		lancamento.setBalanco(balanco);
		lancamento.setDataRegistro(LocalDateTime.now(clock));
		
		balanco.addLancamento(lancamento);
		this.balancoService.atualizarSaldo(balanco);
		
		lancamento = this.repository.save(lancamento);
		
		if(lancamentoDTO.isSalvar()) {
			var lancamentoSalvo = new LancamentoSalvoSalvarDTO(lancamento);
			this.lancamentoSalvoService.cadastrar(balanco.getCategoria().getId(), lancamentoSalvo);
		}
		
		return new LancamentoDTO(lancamento);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public LancamentoDTO alterar(Long id, LancamentoSalvarDTO lancamentoDTO) {
		
		if(id == null) throw new IllegalParameterException("Erro! id não pode ser nullo");
		if(id == 0) throw new IllegalParameterException("Erro! id não pode ser 0");
		
		var optLancamento = this.repository.findById(id);
		
		if(!optLancamento.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro! lançamento não encontrado para o id informado!");
		
		var balanco = optLancamento.get().getBalanco();
		
		if(balanco.getFechado()) 
			throw new IllegalParameterException(
					"Erro! o balanço do lançamento já está fechado!");
		
		//Verifica se o lançamento pertence ao usuario
		this.restricaoService.verificarPermissaoConteudo(optLancamento.get().getBalanco().getCategoria());
		
		var lancamento = optLancamento.get();
		lancamento.setNome(lancamentoDTO.getNome());
		lancamento.setDescricao(lancamentoDTO.getDescricao());
		lancamento.setValor(lancamentoDTO.getValor());
		lancamento.setDataLancamento(lancamentoDTO.getData());
		
		var tipo = this.tipoLancamentoService.buscarPorValor(lancamentoDTO.getTipo());
		
		lancamento.setTipo(tipo);
		
		balanco.rmvLancamento(optLancamento.get());
		balanco.addLancamento(lancamento);
		this.balancoService.atualizarSaldo(balanco);
		
		lancamento = this.repository.save(lancamento);
		
		return new LancamentoDTO(lancamento);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void excluir(Long id) {
		
		var optLancamento = this.repository.findById(id);
		
		if(!optLancamento.isPresent()) 
			throw new ObjectNotFoundFromParameterException(
					"Erro!  lançamento não encontrado para o id informado!");
		
		
		var balanco = optLancamento.get().getBalanco();
		
		if(balanco.getFechado())
			throw new IllegalParameterException("Erro! o balanço do lançamento já está fechado!");
		
		//Verifica se a categoria do balanco pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(balanco.getCategoria());
		
		balanco.rmvLancamento(optLancamento.get());
		
		this.balancoService.atualizarSaldo(balanco);
		
		this.repository.delete(optLancamento.get());
		
	}
	
	public LancamentoDTO buscarPorId(Long id) {
		
		var optLancamento = this.repository.findById(id);
		
		if(!optLancamento.isPresent()) throw new 
			ObjectNotFoundFromParameterException("Erro! lançamento não encontrado para o id informado!");
		
		//Verifica se a categoria do balanco pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(optLancamento.get().getBalanco().getCategoria());
		
		return new LancamentoDTO(optLancamento.get());
		
	}
	
	public Page<LancamentoDTO> listar(Long idBalanco, Pageable paginacao) {
		
		var balanco = this.balancoService.buscarPorId(idBalanco);
			
		//Verifica se a categoria do balanco pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(balanco.getCategoria());
			
		var pageLancamentos = this.repository.findByBalanco(balanco, paginacao);
			
		return pageLancamentos.map(LancamentoDTO::new);
		
	}
	
	public ArquivoDTO gerarArquivoCSV(Long idBalanco) {
		
		var balanco = this.balancoService.buscarPorId(idBalanco);
		
		//Verifica se a categoria do balanco pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(balanco.getCategoria());
		
		var lancamentos = this.repository.findByBalanco(balanco);
		
		String campos[] = {"Data","Tipo","Valor","Nome","Descricao","Data Registro",
				"Saldo Anterior","Saldo Atual"};
		
		var dados = new ArrayList<List<String>>();
		
		for(Lancamento l: lancamentos) {
			dados.add(Arrays.asList(l.getDataRegistro().toString(),l.getTipo().getNome(),
					l.getValor().toString(),l.getNome(),l.getDescricao(), 
						l.getDataRegistro().toString(),"-","-"));
		}
		
		dados.add(Arrays.asList("-","-","-","-","-",balanco.getSaldoAnterior().toString(),
				balanco.getSaldoAtual().toString()));
		
		ByteArrayInputStream byteArrayOutputStream;

		try{
	    	
	    	var out = new ByteArrayOutputStream();
	        var csvPrinter = new CSVPrinter(new PrintWriter(out),
	        		CSVFormat.EXCEL.withDelimiter(';').withHeader(campos));
	        
	        for(List<String> dado: dados) {
	        	csvPrinter.printRecord(dado);
	        }

	        csvPrinter.flush();

	        byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
	    
	    } catch (IOException e) {
	        throw new IllegalParameterException(
	        		"Erro ao tentar gerar arquivo csv: " + e.getMessage());
	    }

	    var fileInputStream = new InputStreamResource(byteArrayOutputStream);
	    
	    var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy_HH:mm:ss");
	  
	    var nomeArquivo = String.format("%s_%s_%s_%s.csv", balanco.getMesAno().getMonth().getValue(), 
	    		balanco.getMesAno().getYear(),balanco.getCategoria().getNome().replaceAll(" ", "-"), 
	    			LocalDateTime.now(clock.getZone()).format(formatter).toString());
	 
	    var arquivoDTO = new ArquivoDTO();
	    arquivoDTO.setNomeArquivo(nomeArquivo);
	    arquivoDTO.setArquivo(fileInputStream);
	    
	    return arquivoDTO;
	    
	}
	
	@Transactional(value = TxType.REQUIRES_NEW, rollbackOn = Exception.class)
	public void tranferir(TransferenciaDTO transferencia) {
		
		var balOrigem = this.balancoService.recuperarAtualInterno(transferencia.getCategoriaOrigem());
		
		var balDestino = this.balancoService.recuperarAtualInterno(transferencia.getCategoriaDestino());
		
		//Transferencia da Origem
		var lancamentoOrigem = new LancamentoSalvarDTO();
		lancamentoOrigem.setNome(transferencia.getNome());
		lancamentoOrigem.setValor(transferencia.getValor());
		lancamentoOrigem.setDescricao(transferencia.getDescricao() 
				+ " - Transferência Destino: " + balDestino.getCategoria().getNome());
		lancamentoOrigem.setTipo(TipoLancamentoEnum.DESPESA.getValor());
		lancamentoOrigem.setData(transferencia.getData());
		
		this.salvar(balOrigem.getId(), lancamentoOrigem);
	
		//Transferencia para o Destino
		var lancamentoDestino = new LancamentoSalvarDTO();
		lancamentoDestino.setNome(transferencia.getNome());
		lancamentoDestino.setValor(transferencia.getValor());
		lancamentoDestino.setDescricao(transferencia.getDescricao() 
				+ " - Transferência Origem: " + balOrigem.getCategoria().getNome());
		lancamentoDestino.setTipo(TipoLancamentoEnum.PROVENTO.getValor());
		lancamentoDestino.setData(transferencia.getData());
		
		this.salvar(balDestino.getId(), lancamentoDestino);

	}
	
}
