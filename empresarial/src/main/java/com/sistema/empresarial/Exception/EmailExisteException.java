package com.sistema.empresarial.Exception;

public class EmailExisteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EmailExisteException(String mensagem) {
		super(mensagem);
	}

}
