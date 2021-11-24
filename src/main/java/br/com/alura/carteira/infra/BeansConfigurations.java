package br.com.alura.carteira.infra;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeansConfigurations { //sempre que quisermo ensinar ao spring como iniciar um novo objeto criamos ele aqui
	
	@Bean //diz ao spring que estou configurando um objeto bean para vc saber dar new nele quando ele for injetado em alguma classe (quando eu colocar o @Autowired nesse objeto, é pra chamar esse método)
	public BCryptPasswordEncoder getPasswordEncoder() { //para ensinar o spring a dar new no atributo bCryptPasswordEncoder, pois só com autowired ele nao sabe
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}
	
}
	