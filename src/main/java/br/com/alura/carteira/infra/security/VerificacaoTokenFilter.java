package br.com.alura.carteira.infra.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.UsuarioRepository;

public class VerificacaoTokenFilter extends OncePerRequestFilter { //a cada requisicao vai chamar esse filter uma vez, por eu estar herdando dessa classe
	
	private TokenService tokenService;
	private UsuarioRepository usuarioRepository;
	
	public VerificacaoTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader("Authorization");
		if (token == null || token.isBlank()) {
			filterChain.doFilter(request, response);// isso aqui fala para o spring seguir o fluxo de execucao, pois o filter vai barrar essa request q nao possui token naturalmente
			return; //para interrompeter a execucao do método aqui.
		}
		
		token = token.replace("Bearer ", "");
		
		boolean tokenValido = tokenService.isValid(token);
		if (tokenValido) {
			Long idUsuario = tokenService.extrairIdUsuario(token);
			Usuario logado = usuarioRepository.carregaPorIdComPerfis(idUsuario).get();
			Authentication authentication = new UsernamePasswordAuthenticationToken(logado, null, logado.getAuthorities());	
			SecurityContextHolder.getContext().setAuthentication(authentication); //essa linha diz ao spring security que o usuário está logado
		}
		
		filterChain.doFilter(request, response);// isso aqui fala para o spring seguir o fluxo de execucao, pois o filter vai barrar essa request q nao possui token naturalmente
		// recuperar o token
		// validar o token
		// se o token estiver correto, liberar a request
		
	} 
	
	

}
