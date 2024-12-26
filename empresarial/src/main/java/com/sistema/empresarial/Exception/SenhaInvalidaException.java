package com.sistema.empresarial.Exception;

public class SenhaInvalidaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SenhaInvalidaException() {
		super("Senha inválida!");
	}
}
