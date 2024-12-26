package com.sistema.empresarial.Controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sistema.empresarial.Dto.CredenciaisDTO;
import com.sistema.empresarial.Dto.TokenDTO;
import com.sistema.empresarial.Entity.Usuario;
import com.sistema.empresarial.Exception.SenhaInvalidaException;
import com.sistema.empresarial.Exception.UsuarioNaoEncontrado;
import com.sistema.empresarial.Service.UsuarioServiceImpl;
import com.sistema.empresarial.Validation.JwtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
@Api("Api Usuários")
public class UsuarioController {

	
	private final UsuarioServiceImpl usuarioService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation("Salvar um novo usuário")
	@ApiResponses({
		@ApiResponse(code = 201,message = "Usuário salvo com sucesso!"),
		@ApiResponse(code = 400,message = "Erro de validação!"),
		@ApiResponse(code = 403,message = "Acesso negado!")
	})
	public Usuario salvar(@RequestBody @Valid Usuario usuario) {
		String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		return usuarioService.salvar(usuario);
	}
	
	@PostMapping("/auth")
	@ApiOperation("Autenticar um usuário e gerar token")
	@ApiResponses({
		@ApiResponse(code = 200,message = "Usuário autenticado com sucesso!"),
		@ApiResponse(code = 401,message = "Credenciais inválidas!"),
		@ApiResponse(code = 403,message = "Acesso negado!")
	})
	public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
		try {
			Usuario usuario = Usuario.builder()
					.email(credenciais.getEmail())
					.senha(credenciais.getSenha())
					.build();
			@SuppressWarnings("unused")
			UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
			String token = jwtService.gerarToken(usuario);
			return new TokenDTO(usuario.getEmail(), token);
		} catch (UsernameNotFoundException | SenhaInvalidaException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	@ApiOperation("Buscar um usuário por ID")
	@ApiResponses({
		@ApiResponse(code = 200,message = "Usuário não encontrado!"),
		@ApiResponse(code = 404,message = "Usuário não encontrado para o ID informado!"),
		@ApiResponse(code = 403,message = "Acesso negado!")	
	})
	public Usuario buscarPorId(@PathVariable Integer id) {
		try {
			return usuarioService.buscarPorId(id);
		} catch (UsuarioNaoEncontrado e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	@ApiOperation("Atualizar um usuário")
	@ApiResponses({
		@ApiResponse(code = 200,message = "Usuário atualizado com sucesso!"),
		@ApiResponse(code = 404,message = "Usuário não encontrado para o ID informado!"),
		@ApiResponse(code = 400,message = "Erro de validação!"),
		@ApiResponse(code = 403,message = "Acesso negado!")
	})
	public Usuario atualizar(@PathVariable Integer id, @RequestBody @Valid Usuario usuarioAtualizado) {
		try {
			return usuarioService.atualizar(id, usuarioAtualizado);
		} catch (UsuarioNaoEncontrado e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation("Deletar um usuário")
	@ApiResponses({
		@ApiResponse(code = 204,message = "Usuário deletado com sucesso!"),
		@ApiResponse(code = 404,message = "Usuário não encontrado para o ID informado!"),
		@ApiResponse(code = 403,message = "Acesso negado!")
	})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Integer id) {
		try {
			 usuarioService.deletar(id);
		} catch (UsuarioNaoEncontrado e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
}
