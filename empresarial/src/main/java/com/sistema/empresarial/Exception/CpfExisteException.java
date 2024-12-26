package com.sistema.empresarial.Exception;

public class CpfExisteException extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//criando metodo para exibir mensagem personalizada
	public CpfExisteException(String mensagem) {
		super(mensagem);
	}
}
