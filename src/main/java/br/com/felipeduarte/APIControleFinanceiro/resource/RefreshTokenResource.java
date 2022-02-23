package br.com.felipeduarte.APIControleFinanceiro.resource;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.felipeduarte.APIControleFinanceiro.resource.exception.AuthorizationException;
import br.com.felipeduarte.APIControleFinanceiro.security.JWTUtil;
import br.com.felipeduarte.APIControleFinanceiro.security.UsuarioDetalhe;
import br.com.felipeduarte.APIControleFinanceiro.security.UsuarioDetalheService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/auth")
public class RefreshTokenResource {

	private UsuarioDetalheService usuarioDetalheService;
	private JWTUtil jwtUtil;
	
	@Autowired
	public RefreshTokenResource(UsuarioDetalheService usuarioDetalheService, JWTUtil jwtUtil) {
		this.usuarioDetalheService = usuarioDetalheService;
		this.jwtUtil = jwtUtil;
	}
	
	@ApiOperation(value = "Refresh do token de acesso")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Token novo gerado com sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PostMapping(value = "/refresh-token")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response){
		
		try {
			Optional<UsuarioDetalhe> optUsuario = this.usuarioDetalheService.getUsuarioAutenticado();
			String token = this.jwtUtil.geradorToken(optUsuario.get());
			response.addHeader("Authorization","Bearer " + token);
			return ResponseEntity.ok().build();
			
		}catch(Exception ex) {
			throw new AuthorizationException(ex.getMessage());
			
		}
		
	}
	
}
