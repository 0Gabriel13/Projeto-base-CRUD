package com.sistema.empresarial.Service;

import javax.transaction.Transactional;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sistema.empresarial.Entity.Usuario;
import com.sistema.empresarial.Exception.SenhaInvalidaException;
import com.sistema.empresarial.Exception.UsuarioExisteException;
import com.sistema.empresarial.Exception.UsuarioNaoEncontrado;
import com.sistema.empresarial.Repository.UsuarioRepository;


@Service
public class UsuarioServiceImpl implements UserDetailsService {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UsuarioRepository repository;

	@Transactional
	public Usuario salvar(@Valid Usuario usuario) {
		boolean exists = repository.findByEmail(usuario.getEmail()).isPresent();
		if (exists) {
			throw new UsuarioExisteException("Já existe um usuário cadastrado com este Email.");
		}

		return repository.save(usuario);
	}

	public UserDetails autenticar(Usuario usuario) {
		UserDetails user = loadUserByUsername(usuario.getEmail());
		boolean senhasBatem = encoder.matches(usuario.getSenha(), user.getPassword());

		if (senhasBatem) {
			return user;
		}
		throw new SenhaInvalidaException();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = repository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));
		
		return User
				.builder()
				.username(usuario.getEmail())
				.password(usuario.getSenha()) //Senha já criptografada no banco
				.roles("User")
				.build();
	}
	
	public Usuario buscarPorId(Integer id) {
		return repository.findById(id)
				.orElseThrow(() -> new UsuarioNaoEncontrado("Usuário não encontrado"));
	}
	
	@Transactional
	public Usuario atualizar(Integer id, Usuario usuarioAtualizado) {
		Usuario usuarioExistente = buscarPorId(id);
		
		//Atualizar email
		usuarioExistente.setEmail(usuarioAtualizado.getEmail());
		
		
		//Atualizar senha se fornecida
		if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
			usuarioExistente.setSenha(encoder.encode(usuarioAtualizado.getSenha()));
		}
		
		//Atualizar outros campos se necessário
		
		return repository.save(usuarioExistente);
	}
	
	@Transactional
	public void deletar (Integer id) {
		Usuario usuario = buscarPorId(id);
		repository.delete(usuario);
	}
}
