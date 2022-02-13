package br.com.felipeduarte.APIControleFinanceiro.service.exception;

public class ObjectNotFoundFromParameterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjectNotFoundFromParameterException(String message) {
		super(message);
	}
	
}
