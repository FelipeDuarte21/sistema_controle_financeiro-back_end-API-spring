package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Categoria;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.Folha;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.ArquivoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.FolhaDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.LancamentoFixoDadosDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.ParcelaPagamentoDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.dto.TransferenciaDTO;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.entidade.enums.TipoLancamento;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.repository.FolhaRepository;
import br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception.IllegalParameterException;

@Service
public class FolhaService {
	
	private Clock clock;
	
	private FolhaRepository repository;
	
	private CategoriaService categoriaService;
	
	private LancamentoService lancamentoService;
	
	private LancamentoFixoService lancamentoFixoService;
	
	private ParceladoService parceladoService;
	
	private ParcelaService parcelaService;
	
	private ContaService contaService;
	
	@Autowired
	public FolhaService(Clock clock, FolhaRepository repository, CategoriaService categoriaService,
			LancamentoService lancamentoService,LancamentoFixoService lancamentoFixoService,
			ParceladoService parceladoService, ParcelaService parcelaService,ContaService contaService) {
		this.clock = clock;
		this.repository = repository;
		this.categoriaService = categoriaService;
		this.lancamentoService = lancamentoService;
		this.lancamentoFixoService = lancamentoFixoService;
		this.parceladoService = parceladoService;
		this.parcelaService = parcelaService;
		this.contaService = contaService;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public FolhaDTO recuperarAtual(Long idCategoria) {
			
		var folhaNova = recuperarAtualInterno(idCategoria);
			
		return new FolhaDTO(folhaNova);
	
	}
	
	@Transactional(rollbackOn = Exception.class)
	public Folha recuperarAtualInterno(Long idCategoria) {
		
		var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
			
		var mesAnoAgora = YearMonth.now(clock.getZone());
			
		var optFolha = this.repository.findByCategoriaAndMesAno(categoria, mesAnoAgora);
			
		if(optFolha.isPresent()) return optFolha.get();
			
		fecharFolha(categoria);
			
		var folhaNova = abrirFolha(categoria);
			
		return folhaNova;
	
	}
	
	@Transactional(rollbackOn = Exception.class)
	private void fecharFolha(Categoria categoria) {
		
		var dataAtual = YearMonth.now(clock.getZone());
		
		var optFolha = this.repository.findByCategoriaAndMesAno(categoria, dataAtual.minusMonths(1L));
		
		if(optFolha.isPresent()) {
			
			var folha = optFolha.get();
			folha.setFechado(true);
			
			this.repository.save(optFolha.get());
		
		}
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	private Folha abrirFolha(Categoria categoria) {
		
		var anoMesAgora = YearMonth.now(clock.getZone());
		
		var anoMesPassado = anoMesAgora.minusMonths(1L);
		
		var optFolhaMesPassado = this.repository.findByCategoriaAndMesAno(categoria, anoMesPassado);
		
		var saldoAnterior = BigDecimal.ZERO;
		
		if(optFolhaMesPassado.isPresent()) 
			saldoAnterior = saldoAnterior.add(optFolhaMesPassado.get().getSaldoAtual());
		
		Folha folha = new Folha();
		folha.setMesAno(anoMesAgora);
		folha.setSaldoAnterior(saldoAnterior);
		folha.setSaldoAtual(saldoAnterior);
		folha.setFechado(false);
		folha.setCategoria(categoria);
		
		return this.repository.save(folha);
		
	}
	
	public List<FolhaDTO> listar(Long idCategoria){
		
		var categoria = this.categoriaService.buscarPorIdInterno(idCategoria);
		
		var folhas = this.repository.findByCategoriaOrderByMesAnoDesc(categoria);
		
		return folhas.stream().map(FolhaDTO::new).collect(Collectors.toList());
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public LancamentoDTO fazerLancamento(Long idFolha, LancamentoDadosDTO lancamentoDadosDTO){
		
		var folha = obterFolha(idFolha);
		
		var saldo = TipoLancamento.toEnum(lancamentoDadosDTO.getTipo())
				.getTipoCalculo().calcular(folha.getSaldoAtual(), lancamentoDadosDTO.getValor());
		
		folha.setSaldoAtual(saldo);
		
		this.repository.save(folha);
		
		var lancamentoDTO = this.lancamentoService.salvar(folha, lancamentoDadosDTO);
		
		if(lancamentoDadosDTO.isSalvar()) salvarLancamentoFixo(folha,lancamentoDadosDTO);
		
		return lancamentoDTO;
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	private void salvarLancamentoFixo(Folha folha, LancamentoDadosDTO lancamentoDadosDTO) {
		
		LancamentoFixoDadosDTO lancamentoFixo = new LancamentoFixoDadosDTO();
		lancamentoFixo.setTitulo(lancamentoDadosDTO.getTitulo());
		lancamentoFixo.setDescricao(lancamentoDadosDTO.getDescricao());
		lancamentoFixo.setValor(lancamentoDadosDTO.getValor());
		lancamentoFixo.setTipo(lancamentoDadosDTO.getTipo());
		
		this.lancamentoFixoService.cadastrar(folha.getCategoria().getId(), lancamentoFixo);
		
	}
	
	public LancamentoDTO alterarLancamento(Long idFolha, Long idLancamento, LancamentoDadosDTO lancamentoDadosDTO) {
		
		var folha = obterFolha(idFolha);
		
		var lancamento = this.lancamentoService.buscarPorIdInterno(idLancamento);
		
		//DESFAZ O LANÇAMENTO
		
		BigDecimal saldo = null;
		
		if(lancamento.getTipo().isProvento()) {
			
			saldo = folha.getSaldoAtual().subtract(lancamento.getValor());
			
			
		}else if(lancamento.getTipo().isDespesa()) {
			
			saldo = folha.getSaldoAtual().add(lancamento.getValor());
			
		}
		
		folha.setSaldoAtual(saldo);
		
		//FAZ O LANÇAMENTO NOVAMENTE - AGORA ALTERADO
		
		saldo = TipoLancamento.toEnum(lancamentoDadosDTO.getTipo()).getTipoCalculo().calcular(folha.getSaldoAtual(),
				lancamentoDadosDTO.getValor());
		
		folha.setSaldoAtual(saldo);
		
		this.repository.save(folha);
		
		return this.lancamentoService.alterar(idLancamento, lancamentoDadosDTO);
		
	}
	
	
	public void excluirLancamento(Long idFolha, Long idLancamento) {
		
		var folha = obterFolha(idFolha);
		
		var lancamento = this.lancamentoService.buscarPorIdInterno(idLancamento);
		
		//DESFAZ O LANÇAMENTO
		
		BigDecimal saldo = null;
		
		if(lancamento.getTipo().isProvento()) {
			
			saldo = folha.getSaldoAtual().subtract(lancamento.getValor());
			
			
		}else if(lancamento.getTipo().isDespesa()) {
			
			saldo = folha.getSaldoAtual().add(lancamento.getValor());
			
		}
		
		folha.setSaldoAtual(saldo);
		
		this.repository.save(folha);
		
		this.lancamentoService.excluir(idLancamento);
		
	}

	@Transactional(rollbackOn = Exception.class)
	public LancamentoDTO fazerLancamentoParcelado(Long idFolha, Long idParcelado, Long idParcela,
			ParcelaPagamentoDTO parcelaPagamentoDTO){
		
		var parcela = this.parcelaService.pagarParcela(idParcela, parcelaPagamentoDTO);
		
		var parcelado = this.parceladoService.atualizarParcelado(idParcelado);
	
		var lancamentoDadosDTO = new LancamentoDadosDTO();
		lancamentoDadosDTO.setTitulo(parcelado.getTitulo());
		lancamentoDadosDTO.setDescricao(
				String.format("Parcela %d de %d", parcela.getNumero(), parcelado.getTotalParcelas()));
		lancamentoDadosDTO.setData(parcelaPagamentoDTO.getDataPagamento());
		lancamentoDadosDTO.setValor(parcelaPagamentoDTO.getValor());
		lancamentoDadosDTO.setTipo(TipoLancamento.DESPESA.getCodigo());
		
		return fazerLancamento(idFolha, lancamentoDadosDTO);
	
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void fazerTransferência(TransferenciaDTO transferenciaDTO){
	
		var folhaOrigem = recuperarAtualInterno(transferenciaDTO.getCategoriaOrigem());
		
		var folhaDestino = recuperarAtualInterno(transferenciaDTO.getCategoriaDestino());
		
		if(folhaOrigem.getSaldoAtual().compareTo(transferenciaDTO.getValor()) == -1)
			throw new IllegalParameterException("Erro! Saldo Insuficiente na categoria de origem!");
		
		//Transferencia da Origem
		
		var lancamentoOrigem = new LancamentoDadosDTO();
		lancamentoOrigem.setTitulo(transferenciaDTO.getTitulo());
		lancamentoOrigem.setValor(transferenciaDTO.getValor());
		lancamentoOrigem.setDescricao(transferenciaDTO.getDescricao() 
				+ " - Transferência De Destino: " + folhaDestino.getCategoria().getNome());
		lancamentoOrigem.setTipo(TipoLancamento.DESPESA.getCodigo());
		lancamentoOrigem.setData(transferenciaDTO.getData());
		
		fazerLancamento(folhaOrigem.getId(), lancamentoOrigem);
	
		//Transferencia para o Destino
		
		var lancamentoDestino = new LancamentoDadosDTO();
		lancamentoDestino.setTitulo(transferenciaDTO.getTitulo());
		lancamentoDestino.setValor(transferenciaDTO.getValor());
		lancamentoDestino.setDescricao(transferenciaDTO.getDescricao() 
				+ " - Transferência Origem: " + folhaOrigem.getCategoria().getNome());
		lancamentoDestino.setTipo(TipoLancamento.PROVENTO.getCodigo());
		lancamentoDestino.setData(transferenciaDTO.getData());
		
		fazerLancamento(folhaDestino.getId(), lancamentoDestino);
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void fazerLancamentosParaTodasCategorias(Long idConta) {
		
		var conta = this.contaService.buscarPorId(idConta);
		
		conta.getCategorias().forEach(categoria -> {
			
			var folha = recuperarAtualInterno(categoria.getId());
			
			var valorCalculado = (conta.getRendaMensalTotal().doubleValue() * categoria.getPorcentagem()) / 100;
			
			var valor = BigDecimal.valueOf(valorCalculado).setScale(2, RoundingMode.HALF_EVEN);
			
			var lancamentoProvento = new LancamentoDadosDTO();
			lancamentoProvento.setTitulo("Parte da renda mensal total reservada a categoria");
			lancamentoProvento.setDescricao(
					String.format("Provento adicionado pelo sistema a pedido do usuário. %s . %s porcento da renda mensal total",
							categoria.getNome(),categoria.getPorcentagem().toString()));
			lancamentoProvento.setValor(valor);
			lancamentoProvento.setTipo(TipoLancamento.PROVENTO.getCodigo());
			lancamentoProvento.setData(LocalDate.now(clock));
			lancamentoProvento.setSalvar(false);
			
			fazerLancamento(folha.getId(), lancamentoProvento);
			
		});
		
	}
	
	private Folha obterFolha(Long idFolha) {
		var optFolha = this.repository.findById(idFolha);
		
		if(optFolha.isEmpty()) throw new IllegalParameterException("Erro! folha não encontrada!");
		
		if(optFolha.get().getFechado()) throw new IllegalParameterException("Erro! a folha já foi fechada!");
		
		return optFolha.get();
	}

	public ArquivoDTO gerarArquivoCSV(Long idFolha) {
		
		var optFolha = this.repository.findById(idFolha);
		
		if(optFolha.isEmpty()) throw new IllegalParameterException("Erro! folha não encontrada!");
		
		var folha = optFolha.get();
		
		var lancamentos = this.lancamentoService.listar(folha);
		
		String campos[] = {"Data","Tipo","Valor","Titulo","Descricao","Data Registro",
				"Saldo Anterior","Saldo Atual"};
		
		var dados = new ArrayList<List<String>>();
		
		for(LancamentoDTO lancamento: lancamentos) {
			dados.add(Arrays.asList(lancamento.getDataRegistro().toString(),lancamento.getTipo(),
					lancamento.getValor().toString(),lancamento.getTitulo(),lancamento.getDescricao(), 
						lancamento.getDataRegistro().toString(),"-","-"));
		}
		
		dados.add(Arrays.asList("-","-","-","-","-","-",folha.getSaldoAnterior().toString(),
				folha.getSaldoAtual().toString()));
		
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
	  
	    var nomeArquivo = String.format("%s_%s_%s_%s.csv", folha.getMesAno().getMonth().getValue(), 
	    		folha.getMesAno().getYear(),folha.getCategoria().getNome().replaceAll(" ", "-"), 
	    			LocalDateTime.now(clock.getZone()).format(formatter).toString());
	 
	    var arquivoDTO = new ArquivoDTO();
	    arquivoDTO.setNomeArquivo(nomeArquivo);
	    arquivoDTO.setArquivo(fileInputStream);
	    
	    return arquivoDTO;
	    
	}
	
}
