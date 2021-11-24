package br.com.alura.carteira.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Error400Dto {
	
	private String campo;
	private String mensagem;

}
