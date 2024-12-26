package com.sistema.empresarial.Exception;

public class EmailInvalidoException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailInvalidoException() {
		super("Este email está inválido!");
	}

}
