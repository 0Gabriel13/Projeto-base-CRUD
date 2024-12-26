package com.sistema.empresarial.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.empresarial.Entity.Cliente;
import com.sistema.empresarial.Repository.Projection.ClienteProjection;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	@Query(value = "SELECT * FROM cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))", nativeQuery = true)
	List<Cliente> encontrarPorNome(@Param ("nome") String nome);
	
	@Query(value = "SELECT c.id, c.nome FROM cliente c", nativeQuery = true)
	List<ClienteProjection> findAllProjections();
	
	@Query("DELETE FROM Cliente c WHERE c.nome = :nome") //JPQL
	@Modifying
	void deleteByNome(@Param("nome") String nome);
	
	boolean existsByNome(String nome);
	
	boolean existsByCpf(String cpf);
	
	boolean existsByEmail(String email);
	
}
