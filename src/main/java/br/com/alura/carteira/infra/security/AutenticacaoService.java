package br.com.alura.carteira.infra.security;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alura.carteira.dto.LoginFormDto;
import br.com.alura.carteira.repository.UsuarioRepository;

@Service //precisa dessa anotacao se nao o spring nao vai enxergar a classe e nao conseguira injetar o repository nela
public class AutenticacaoService implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private AuthenticationManager authenticationManager; // classe que cuida do controle de autenticacao do projeto, para forçar o spring disparar o processo de autenticação, precisamos usar essa classe

	@Autowired
	private TokenService tokenService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return repository
				.findByLogin(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
	}

	public String autenticar(LoginFormDto dto) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				dto.getLogin(), 
				dto.getSenha()); //objeto que representa o login e a senha do spring
		authentication = authenticationManager.authenticate(authentication); //vai disparar o processo de autenticacao, chamar o noso loadByUsername
		
		return tokenService.gerarToken(authentication);
	}

}
