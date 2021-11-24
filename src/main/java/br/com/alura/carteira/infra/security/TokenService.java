package br.com.alura.carteira.infra.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.carteira.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class TokenService {
	
	@Value("${jjwtmaspodeserqualquernomeaqui.secret}")//vai ler o application properties e procurar essa propriedade e injetar o valor dela
	private String secret;

	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal();
		return Jwts
				.builder()
				.setSubject(logado.getId().toString())  //estamos gerando token para um usuário , entao precisamos informar o objeto usuario daquele token dele, pois teremos varios tokens temos que saber qual token é de qual. e passamos como parametro nele o object do user logado,
				.signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secret)//para assinar o token usando algum algoritimo de criptografia, o segundo parametro é o secret que é pra ler o token para descriptografar;
				.compact(); //método que vai compactar o token em uma string
 	}
	
	public boolean isValid(String token) {
		try {
			Jwts
			.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long extrairIdUsuario(String token) {
		Claims claims = Jwts
		.parser()
		.setSigningKey(secret)
		.parseClaimsJws(token)
		.getBody();
		return Long.parseLong(claims.getSubject()); //getSubject pois foi no subject que setamos o id do user
		

	}
}
