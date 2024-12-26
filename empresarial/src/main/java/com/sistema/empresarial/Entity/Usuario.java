package com.sistema.empresarial.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "{campo.email.obrigatório}")
	@Email(message = "O email informado não é válido")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$" , message = "O email deve fornecer um endereço @gmail.com")
	private String email;
	
	@NotEmpty(message = "{campo.senha.obrigatório}")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Modo para não retornar a senha na resposta JSON
	private String senha;
	
	@Column(nullable = true)
	private boolean active;
}
