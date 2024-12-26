package com.sistema.empresarial.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.br.CPF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nome;
	
	@CPF
	@Column(name = "cpf", length = 11)
	@NotEmpty(message = "{campo.cpf.obrigatório}")
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Modo para não retornar o cpf na resposta JSON
	private String cpf;
	
	@NotEmpty(message = "{campo.email.obrigatório}")
	@Email(message = "O email informado não é válido")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$" , message = "O email deve fornecer um endereço @gmail.com")
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Modo para não retornar o email na resposta JSON
	private String email;
	
	@NotEmpty(message = "{campo.telefone.obrigatório}")
	@Column(name = "telefone", length = 11)
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Modo para não retornar o telefone na resposta JSON
	private String telefone;
	
	@OneToOne
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Modo para não retornar o endereco na resposta JSON
	private Endereco endereco;
	
	public Cliente(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}

}
