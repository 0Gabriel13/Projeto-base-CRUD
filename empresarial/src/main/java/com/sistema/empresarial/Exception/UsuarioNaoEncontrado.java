package com.sistema.empresarial.Exception;

public class UsuarioNaoEncontrado extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioNaoEncontrado(String mensagem) {
		super(mensagem);
	}
}
