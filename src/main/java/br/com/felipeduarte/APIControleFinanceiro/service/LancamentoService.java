package br.com.felipeduarte.APIControleFinanceiro.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.felipeduarte.APIControleFinanceiro.model.Balanco;
import br.com.felipeduarte.APIControleFinanceiro.model.Lancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.TipoLancamento;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.ArquivoDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LancamentoDTO;
import br.com.felipeduarte.APIControleFinanceiro.repository.LancamentoRepository;

@Service
public class LancamentoService {
	
	private static final String TIME_ZONE = "America/Sao_Paulo";
	
	@Autowired
	private LancamentoRepository repository;
	
	@Autowired
	private BalancoService balancoService;
	
	@Autowired
	private TipoLancamentoService tipoLancamentoService;
	
	@Autowired
	private RestricaoService restricaoService;
	
	
	public Lancamento salvar(LancamentoDTO lancamento) {
		
		Balanco balanco = this.balancoService.buscarPorId(lancamento.getBalanco());
		if(balanco == null || balanco.getFechado()) {
			return null;
		}
		
		if(balanco.getFechado()) return null;
		
		TipoLancamento tipoLancamento = this.tipoLancamentoService.buscarPorValor(lancamento.getTipo());
		if(tipoLancamento == null) {
			return null;
		}
		
		//Verifica se a categoria do balanco pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(balanco.getCategoria());
		
		Lancamento lan = Lancamento.converteParaLancamento(lancamento);
		
		lan.setTipo(tipoLancamento);
		lan.setBalanco(balanco);
		
		//Tratando a data
		if(lan.getDataCadastro() == null) {
			lan.setDataCadastro(LocalDate.now(ZoneId.of(TIME_ZONE)));
		}
		
		lan = this.repository.save(lan);
		
		this.balancoService.atualizarSaldo(lan.getBalanco());
		
		return lan;
	}
	
	public Lancamento alterar(LancamentoDTO lancamento) {
		
		if(lancamento.getId() == null || lancamento.getId() == 0) {
			return null;
		}
		
		Lancamento lan = this.buscarPorId(lancamento.getId());
		if(lan == null) {
			return null;
		}
		
		lan = this.salvar(lancamento);
		
		return lan;
	}
	
	public boolean excluir(Long id) {
		
		Lancamento lancamento = this.buscarPorId(id);
		
		if(lancamento == null || lancamento.getBalanco().getFechado()) {
			return false;
		}
		
		Balanco balanco = lancamento.getBalanco();
		
		//Verifica se a categoria do balanco pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(balanco.getCategoria());
		
		this.repository.delete(lancamento);
		
		balanco.rmvLancamento(lancamento);
		
		this.balancoService.atualizarSaldo(balanco);
		
		return true;
	}
	
	public Lancamento buscarPorId(Long id) {
		
		Optional<Lancamento> lancamento = this.repository.findById(id);
		
		if(lancamento.isEmpty()) {
			return null;
		}
		
		//Verifica se a categoria do balanco pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(lancamento.get().getBalanco().getCategoria());
		
		return lancamento.get();
	}
	
	public Page<Lancamento> buscarPorBalanco(Long idBalanco, Integer page, Integer size, Integer order) {
		
		Balanco balanco = this.balancoService.buscarPorId(idBalanco);
		
		if(balanco == null) {
			return null;
		}
		
		//Verifica se a categoria do balanco pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(balanco.getCategoria());
		
		Direction d = Direction.DESC;
		
		if(order == 1) {
			d = Direction.ASC;
		}else if(order == 2) {
			d = Direction.DESC;
		}
		
		PageRequest pageable = PageRequest.of(page, size, d, "dataCadastro");
		
		Page<Lancamento> lancamentos = this.repository.findByBalanco(balanco, pageable);
		
		return lancamentos;
		
	}
	
	public ArquivoDTO gerarArquivoCSV(Long idBalanco) {
		
		Balanco balanco = this.balancoService.buscarPorId(idBalanco);
		
		if(balanco == null) {
			return null;
		}
		
		//Verifica se a categoria do balanco pertence ao usuario logado
		this.restricaoService.verificarPermissaoConteudo(balanco.getCategoria());
		
		List<Lancamento> lancamentos = this.repository.findByBalanco(balanco);
		
		String campos[] = {"Data","Tipo","Valor","Nome","Descricao","Saldo Anterior","Saldo Atual"};
		
		List<List<String>> dados = new ArrayList<>();
		
		for(Lancamento l: lancamentos) {
			dados.add(Arrays.asList(l.getDataCadastro().toString(),l.getTipo().getNome(),
					l.getValor().toString(),l.getNome(),l.getDescricao(), "-","-"));
		}
		
		dados.add(Arrays.asList("-","-","-","-","-",balanco.getSaldoAnterior().toString(),
				balanco.getSaldoAtual().toString()));
		
		ByteArrayInputStream byteArrayOutputStream;

		try{
	    	
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
	        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out),
	        		CSVFormat.EXCEL.withDelimiter(';').withHeader(campos));
	        
	        for (List<String> dado : dados) {
	            csvPrinter.printRecord(dado);
	        }

	        csvPrinter.flush();

	        byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
	    
	    } catch (IOException e) {
	        System.err.println("Erro ao tentar gerar arquivo csv: " + e.getMessage());
	        return null;
	    }

	    InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);
	    
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy_HH:mm:ss");
	  
	    String nomeArquivo = String.format("%s_%s_%s_%s.csv", balanco.getMes(), balanco.getAno(),
	    		balanco.getCategoria().getNome().replaceAll(" ", "-"), 
	    			LocalDateTime.now(ZoneId.of(TIME_ZONE)).format(formatter).toString());
	 
	    ArquivoDTO arquivoDTO = new ArquivoDTO();
	    arquivoDTO.setNomeArquivo(nomeArquivo);
	    arquivoDTO.setArquivo(fileInputStream);
	    
	    return arquivoDTO;
	    
	}
	
}
