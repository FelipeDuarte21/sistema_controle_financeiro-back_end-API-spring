package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.service.exception;

public class ObjectNotFoundFromParameterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjectNotFoundFromParameterException(String message) {
		super(message);
	}
	
}
