package br.com.felipeduarte.APIControleFinanceiro.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.ParcelaDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.ParceladoDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.ParceladoSalvarDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectNotFoundException;
import br.com.felipeduarte.APIControleFinanceiro.service.ParceladoService;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.ObjectNotFoundFromParameterException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/categorias/{idCategoria}/parcelados")
public class ParceladoResource {
	
	public ParceladoService service;
	
	@Autowired
	public ParceladoResource(ParceladoService service) {
		this.service = service;
	}
	
	@ApiOperation(value = "Cadastra uma novo parcelamento")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Parcelamento cadastrado com sucesso"),
			@ApiResponse(code = 400, message = "Categoria não encontrada"),
			@ApiResponse(code = 401, message = "Acesso Indevido"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping(produces = "application/json",consumes = "application/json")
	public ResponseEntity<ParceladoDTO> cadastrar(@PathVariable(name = "idCategoria") Long idCategoria, 
			@RequestBody @Valid ParceladoSalvarDTO parceladoDTO, UriComponentsBuilder uriBuilder){
		
		try {
			
			var parcelado = this.service.cadastrar(idCategoria, parceladoDTO);
			
			var uri = uriBuilder.path("api/categorias/{idCategoria}/parcelados/{id}")
					.buildAndExpand(parcelado.getCategoria().getId(),parcelado.getId()).toUri();
			
			return ResponseEntity.created(uri).body(parcelado);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	

	@ApiOperation(value = "Atualiza um parcelamento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parcelamento atualizado com sucesso"),
			@ApiResponse(code = 401, message = "Acesso Indevido"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Parcelamento não encontrado"),
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping(value = "/{id}", produces = "application/json",consumes = "application/json")
	public ResponseEntity<ParceladoDTO> atualizar(@PathVariable(name = "id") Long id, 
			@RequestBody @Valid ParceladoSalvarDTO parceladoDTO){
		
		try {
			
			var parcelado = this.service.alterar(id, parceladoDTO);
			
			return ResponseEntity.ok(parcelado);
			
		}catch (IllegalParameterException  ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
		
	}
	
	@ApiOperation(value = "Exclui um parcelamento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parcelamento excluído com sucesso"),
			@ApiResponse(code = 401, message = "Acesso Indevido"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Parcelamento não encontrado"),
	})
	@PreAuthorize("hasAnyRole('USER')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.service.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
		
	}
	
	@ApiOperation(value = "Buscar um parcelamento pela identificação")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parcelamento encontrado com sucesso"),
			@ApiResponse(code = 401, message = "Acesso Indevido"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Parcelamento não encontrado"),
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<ParceladoDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		try {
			
			var parcelado = this.service.buscarPorId(id);
			
			return ResponseEntity.ok(parcelado);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
		
	}
	
	@ApiOperation(value = "Buscar página de parcelamentos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pagina de parcelamento encontrada com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos parametros"),
			@ApiResponse(code = 403, message = "Acesso negado"),
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(produces = "application/json")
	public ResponseEntity<Page<ParceladoDTO>> listar(
			@PathVariable(name = "idCategoria") Long idCategoria, 
			@RequestParam(defaultValue = "0") Integer page, 
			@RequestParam(defaultValue = "6") Integer size, 
			@RequestParam(defaultValue = "2") Integer order){
		
		try {
			
			var pageParcelados = this.service.listar(idCategoria, page, size, order);
			
			return ResponseEntity.ok(pageParcelados);
			
		}catch (IllegalParameterException  ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
		
	}
	
	@ApiOperation(value = "Registrar pagamento da parcela do parcelamento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parcela paga com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos parametros"),
			@ApiResponse(code = 401, message = "Acesso Indevido"),
			@ApiResponse(code = 403, message = "Acesso negado"),
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PatchMapping("/parcelas/{idParcela}")
	public ResponseEntity<ParcelaDTO> pagarParcela(@PathVariable(name = "idCategoria") Long idCategoria,
			@PathVariable(name = "idParcela") Long idParcela){
		
		try {
			
			var parcela = this.service.pagarParcela(idCategoria, idParcela);
			
			return ResponseEntity.ok(parcela);
			
		}catch (IllegalParameterException  ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
		
	}

}
