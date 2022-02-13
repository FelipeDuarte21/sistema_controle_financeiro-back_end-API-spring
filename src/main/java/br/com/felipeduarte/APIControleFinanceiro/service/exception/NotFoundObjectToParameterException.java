package br.com.felipeduarte.APIControleFinanceiro.service.exception;

public class NotFoundObjectToParameterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotFoundObjectToParameterException(String message) {
		super(message);
	}
	
}
