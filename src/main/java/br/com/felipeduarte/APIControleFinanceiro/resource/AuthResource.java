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
import br.com.felipeduarte.APIControleFinanceiro.model.dto.LoginDTO;
import br.com.felipeduarte.APIControleFinanceiro.model.dto.TokenDTO;
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
	
	@ApiOperation(value = "Obter o token de acesso")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Token retornado com sucesso"),
			@ApiResponse(code = 400, message = "Login e/ou senha estão errados")
	})
	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO login){
		
		try {
			
			var token = this.authService.login(login);
			
			return ResponseEntity.ok(token);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Refresh do token de acesso")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Token novo gerado com sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PostMapping(value = "/refresh-token")
	public ResponseEntity<TokenDTO> refreshToken(HttpServletResponse response){
		
		try {
			
			TokenDTO token = this.authService.refreshToken();
			
			return ResponseEntity.ok(token);
			
		}catch(Exception ex) {
			throw new AuthorizationException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Reset de senha do usuario")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Nova senha gerada com sucesso"),
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
