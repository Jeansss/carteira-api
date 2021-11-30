package br.com.alura.carteira.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Profile("prod") //só carrega se o profile ativo for prod
public class EnviadorDeEmailReal implements EnviadorDeEmail {
	
	//classe que sabe enviar o email
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	@Async
	public void enviarEmail(String destinatario, String assunto, String mensagem) {
		SimpleMailMessage email = new SimpleMailMessage(); //esse objeto representa o email mesmo, com destinatario, assunto, mensagem
		email.setTo(destinatario);
		email.setSubject(assunto);
		email.setText(mensagem);
		
		mailSender.send(email);
	}

}
