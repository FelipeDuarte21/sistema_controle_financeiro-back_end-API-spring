package br.com.felipeduarte.APIControleFinanceiro.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.TipoLancamentoDTO;
import br.com.felipeduarte.APIControleFinanceiro.service.TipoLancamentoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/tipos-lancamentos")
public class TipoLancamentoResource {
	
	private TipoLancamentoService service;
	
	@Autowired
	public TipoLancamentoResource(TipoLancamentoService service) {
		this.service = service;
	}
	
	@ApiOperation(value = "Buscar os tipo de lançamentos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipos de lançamentos encontrado"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(produces = "application/json")
	public ResponseEntity<List<TipoLancamentoDTO>> listar(){
		
		var tipos = this.service.listar();
		
		return ResponseEntity.ok(tipos);
		
	}
	
}
