package br.com.felipeduarte.APIControleFinanceiro.resource;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.felipeduarte.APIControleFinanceiro.model.dto.EmailDTO;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.AuthorizationException;
import br.com.felipeduarte.APIControleFinanceiro.resource.exception.ObjectBadRequestException;
import br.com.felipeduarte.APIControleFinanceiro.service.AuthService;
import br.com.felipeduarte.APIControleFinanceiro.service.exception.IllegalParameterException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/auth")
public class AuthResource {

	private AuthService authService;

	@Autowired
	public AuthResource(AuthService authService) {
		this.authService = authService;
	}
	
	@ApiOperation(value = "Refresh do token de acesso")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Token novo gerado com sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PostMapping(value = "/refresh-token")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response){
		
		try {
			
			String token = this.authService.refreshToken();
			response.addHeader("Authorization","Bearer " + token);
			
			return ResponseEntity.noContent().build();
			
		}catch(Exception ex) {
			throw new AuthorizationException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Reset de senha do usuario")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Nova senha gerada com sucesso"),
			@ApiResponse(code = 400, message = "Usuario não encontrado para email informado")
	})
	@PostMapping(value = "/reset-senha")
	public ResponseEntity<Void> resetSenha(@RequestBody @Valid EmailDTO email){
		
		try {
			
			this.authService.resetSenha(email);
			
			return ResponseEntity.noContent().build();
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
}
