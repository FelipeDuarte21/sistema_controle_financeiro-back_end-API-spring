package br.com.felipeduarte.APIControleFinanceiro.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.BalancoDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.BalancoFaixaDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.service.BalancoService;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/categorias/{idCategoria}/balancos")
public class BalancoResource {
	
	private BalancoService service;
	
	@Autowired
	public BalancoResource(BalancoService service) {
		this.service = service;
	}
	
	@ApiOperation(value = "Busca um resumo dos balanços em torno de uma data")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna os balanços"),
			@ApiResponse(code = 400, 
				message = "Erro nos parâmetros recebidos ou balanço central não econtrado"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(value = "/faixa", produces = "application/json")
	public ResponseEntity<List<BalancoFaixaDTO>> buscarFaixas(
			@PathVariable(name = "idCategoria") Long idCategoria,
			@RequestParam(name = "ano") Integer ano, 
			@RequestParam(name = "mes") Integer mes, 
			@RequestParam(name = "qtdMes") Integer qtdMes){
		
		try {
			
			var balancos = this.service.buscarFaixas(idCategoria, ano, mes, qtdMes);
			
			return ResponseEntity.ok(balancos);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Busca o balanço aberto da categoria")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Balanço encontrado"),
			@ApiResponse(code = 400, message = "Erro nos paramêtros recebidos"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(value = "/atual", produces = "application/json")
	public ResponseEntity<BalancoDTO> recuperarAtual(@PathVariable(name = "idCategoria") Long idCategoria){
		
		try {
			
			var balanco = this.service.recuperarAtual(idCategoria);
			
			return ResponseEntity.ok(balanco);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Busca balanço de uma determinada data")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Balanço encontrado"),
			@ApiResponse(code = 400, message = "Erro nos paramêtros recebidos"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(value = "/data", produces = "application/json")
	public ResponseEntity<BalancoDTO> buscarPorData(
		@PathVariable(name = "idCategoria") Long idCategoria, 
		@RequestParam Integer mes,
		@RequestParam Integer ano){
		
		try {
			
			var balanco = this.service.buscarPorData(idCategoria, mes, ano);
			
			return ResponseEntity.ok(balanco);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
}
