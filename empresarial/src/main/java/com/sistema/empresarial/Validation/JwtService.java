package com.sistema.empresarial.Validation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sistema.empresarial.Entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// chave assinatura : Jesus e minha vida
@Service
public class JwtService {

	@Value("${security.jwt.expiracao}")
	private String expiracao;
	
	@Value("${security.jwt.chave-assinatura}")
	private String chaveAssinatura;
	
	//Metdo para gerar o token
	public String gerarToken(Usuario usuario) {
		Long expString = Long.valueOf(expiracao);
		LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
		Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
		Date data = Date.from(instant);
		
		return Jwts.builder().setSubject(usuario.getEmail()).setExpiration(data)
				.signWith(SignatureAlgorithm.HS512, chaveAssinatura).compact();
	}
	
	//Valida e extrai as informações (claims) de um token JWT.
	private Claims obterClaims (String token) throws ExpiredJwtException{
		return Jwts.parser()
				.setSigningKey(chaveAssinatura). // Define a chave usada para validar a assinatura do token
				parseClaimsJws(token). // Faz o parsing e valida a assinatura
				getBody(); // Retorna o corpo (claims) do token JWT
	}
	
	public boolean tokenValido(String token) {
		try {
			Claims claims = obterClaims(token);
			Date dataExpiacao = claims.getExpiration();
			LocalDateTime data = dataExpiacao.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			return !LocalDateTime.now().isAfter(data);
		} catch (Exception e) {
			return false;
		}
	}
	
	public String obterLoginUsuario(String token) throws ExpiredJwtException{
		return (String) obterClaims(token).getSubject();
	}
	
	
}
