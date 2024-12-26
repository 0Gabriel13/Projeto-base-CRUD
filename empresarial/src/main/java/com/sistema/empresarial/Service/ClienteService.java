package com.sistema.empresarial.Service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.empresarial.Entity.Cliente;
import com.sistema.empresarial.Exception.CpfExisteException;
import com.sistema.empresarial.Exception.EmailExisteException;
import com.sistema.empresarial.Repository.ClienteRepository;
import com.sistema.empresarial.Repository.EnderecoRepository;

@Service
public class ClienteService {

	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente salvar(@Valid Cliente cliente) {
		if (clienteRepository.existsByCpf(cliente.getCpf())) {
			throw new CpfExisteException("Já existe um cliente cadastrado com este CPF.");
		}
		if (clienteRepository.existsByEmail(cliente.getEmail())) {
			throw new EmailExisteException("Já existe um cliente cadastrado com este Email.");
		}
		
		if (cliente.getEndereco() != null ) {
			cliente.setEndereco(enderecoRepository.save(cliente.getEndereco()));
		}
		return clienteRepository.save(cliente);
	}
}
