package com.sistema.empresarial.Controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sistema.empresarial.Entity.Cliente;
import com.sistema.empresarial.Entity.Endereco;
import com.sistema.empresarial.Repository.ClienteRepository;
import com.sistema.empresarial.Repository.EnderecoRepository;
import com.sistema.empresarial.Repository.Projection.ClienteProjection;
import com.sistema.empresarial.Service.ClienteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/cliente")
@Api("Api Clientes")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping("{id}")
	@ApiOperation("Obter detalhes de um cliente.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Cliente encontrado."),
		@ApiResponse(code = 404, message = "Cliente não encontrado para o ID informado!")
	})
	public Cliente getClienteById(@PathVariable Integer id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado!"));
	}
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses({
		@ApiResponse(code = 201, message = "Cliente salvo com sucesso."),
		@ApiResponse(code = 400, message = "Erro de validação!")
	})
	public Cliente save(@RequestBody @Valid Cliente cliente) {
		return clienteService.salvar(cliente);
	}
	
	
	@DeleteMapping("{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ApiOperation("Deletar um cliente.")
	@ApiResponses({
		@ApiResponse(code = 204, message = "Cliente deletado com sucesso."),
		@ApiResponse(code = 404, message = "Cliente não encontrado para o ID informado!")
	})
	public void delete(@PathVariable Integer id) {
		clienteRepository.findById(id)
		.map(cliente -> {
			clienteRepository.delete(cliente);
			return cliente;
		})
		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Cliente não encontrado!"));
	}
	
	
	@PutMapping("{id}")
	@ResponseStatus(code = HttpStatus.OK)
	@ApiOperation("Atualizar um cliente.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Cliente atualizado com sucesso."),
		@ApiResponse(code = 404, message = "Cliente não encontrado para o ID informado!")
	})
	public Cliente update(@PathVariable Integer id, @RequestBody Cliente cliente) {
		return clienteRepository.findById(id)
				.map(clienteExistente -> {
					cliente.setId(id);
					
					//verifica se um novo endereço foi enviado na requisiçao
					if (cliente.getEndereco() != null) {
						Endereco enderecoAtualizado = cliente.getEndereco();
						enderecoAtualizado.setId(clienteExistente.getEndereco().getId()); //mantém o id do endereco
						enderecoRepository.save(enderecoAtualizado);
						cliente.setEndereco(enderecoAtualizado);
					} else {
						cliente.setEndereco(clienteExistente.getEndereco()); //Mantem o endereco antigo caso nao tenha fornecido um novo
					}
					return clienteRepository.save(cliente);
				}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado!"));
	}
	
	
	@GetMapping
	@ApiOperation("Buscar cliente com base em filtros.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Clientes encontrados com base de filtros."),
		@ApiResponse(code = 404, message = "Erro de validação nos filtros!")
	})
	public List<ClienteProjection> find(Cliente filtro){
		return clienteRepository.findAllProjections();
	}
	
	
	@GetMapping("/buscar")
	@ApiOperation("Buscar clientes por nome.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Cliente encontrado."),
		@ApiResponse(code = 404, message = "Nenhum cliente encontrado com o nome fornecido!")
	})
	public List<Cliente> buscarPorNome(@RequestParam("nome") String nome){
		List<Cliente> clientes = clienteRepository.encontrarPorNome("%" + nome + "%");
		if (clientes.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum cliente encontrado com o nome: " + nome);
		}
		
		return clientes;
	}
}
