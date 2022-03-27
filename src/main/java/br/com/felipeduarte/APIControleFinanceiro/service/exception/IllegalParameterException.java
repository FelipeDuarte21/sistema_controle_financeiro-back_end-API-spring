package br.com.felipeduarte.APIControleFinanceiro.service.exception;

public class IllegalParameterException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public IllegalParameterException(String message) {
		super(message);
	}

}
