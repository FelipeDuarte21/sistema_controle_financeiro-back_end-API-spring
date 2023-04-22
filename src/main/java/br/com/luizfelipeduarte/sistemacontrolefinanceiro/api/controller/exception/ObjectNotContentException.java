package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class ObjectNotContentException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ObjectNotContentException(String message) {
		super(message);
	}

}
