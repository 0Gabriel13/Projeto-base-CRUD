package com.sistema.empresarial.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "{campo.cidade.obrigatório}")
	private String cidade;
	
	@NotEmpty(message = "{campo.cep.obrigatório}")
	private String cep;
	
	@NotEmpty(message = "{campo.bairro.obrigatório}")
	private String bairro;
	
	@NotEmpty(message = "{campo.rua.obrigatório}")
	private String rua;

	@NotEmpty(message = "{campo.numeroResidencia.obrigatório}")
	private String numeroResidencia;
	
	
	
}
