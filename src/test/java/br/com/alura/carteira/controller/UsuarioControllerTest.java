package br.com.alura.carteira.controller;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.alura.carteira.infra.security.TokenService;
import br.com.alura.carteira.modelo.Perfil;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.PerfilRepository;
import br.com.alura.carteira.repository.UsuarioRepository;


@ExtendWith(SpringExtension.class)
@SpringBootTest  //serve para o spring carregar o projeto inteiro, pois é necessário para testar o controller
@AutoConfigureMockMvc // anotacao para o teste configurar o Mock mvc que vou usa ali abaixo, eai sim consigo injeta lo
@ActiveProfiles("test") //para poder usar o banco de dados de teste para o teste
@Transactional //para ele rodar os testes no contexto transacional e fazer o rollback ao final de cada teste
class UsuarioControllerTest {

	@Autowired
	private MockMvc mvc; //para simular requests http como no postman
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private PerfilRepository perfilRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private String token;
	
	@BeforeEach
	public void gerarToken() {
		Usuario logado = new Usuario("Rodrigo", "rodrigo", "123456");
		Perfil admin = perfilRepository.findById(1l).get();
		logado.adicionarPerfil(admin);
		usuarioRepository.save(logado);
				
		Authentication authentication = new UsernamePasswordAuthenticationToken(logado, logado.getLogin());
		this.token = tokenService.gerarToken(authentication);
	}
	
	@Test
	void naoDeveriaCadastrarUsuariosComDadosIncompletos() throws Exception {
		String json = "{}";
		mvc.perform(MockMvcRequestBuilders
				.post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json) //para o spring performar uma request do tipo post para o /usuarios , com a string json no body
				.header("Authorization", "Bearer " + token))  
				.andExpect(MockMvcResultMatchers.status().isBadRequest()); //próprio validador do mockmvc, dai nao precisamos do assertj ou junit
	}
	
	@Test
	void deveriaCadastrarUsuariosComDadosCompletos() throws Exception {
		String json = "{\"nome\":\"jean\",\"login\":\"jean@test.com\",\"perfilId\":1}";
		String jsonEsperado = "{\"nome\":\"jean\",\"login\":\"jean@test.com\"}";

		mvc.perform(MockMvcRequestBuilders
				.post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json) //para o spring performar uma request do tipo post para o /usuarios , com a string json no body
				.header("Authorization", "Bearer " + token))  
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().exists("Location"))
				.andExpect(MockMvcResultMatchers.content().json(jsonEsperado)); //próprio validador do mockmvc, dai nao precisamos do assertj ou junit
			
	}

}
