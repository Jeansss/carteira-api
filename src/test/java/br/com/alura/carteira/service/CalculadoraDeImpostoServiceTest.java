package br.com.alura.carteira.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.carteira.modelo.TipoTransacao;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;

class CalculadoraDeImpostoServiceTest {
	
	private CalculadoraDeImpostoService calculadora;

	private Transacao criarTransacao(BigDecimal preco, Integer quantidade, TipoTransacao tipo) {
		Transacao t = new Transacao(
				1l, 
				"TEST", 
				LocalDate.now(),
				preco,
				quantidade,
				tipo,
				new Usuario(2l, "Jean", "test@test.com", "123", null ),
				BigDecimal.ZERO);
		return t;
	}
	
	@BeforeEach
	public void inicializar() {
		calculadora = new CalculadoraDeImpostoService();
	}

	@Test
	void transacaoDoTipoCompraNaoDeveriaTerImposto() {
		Transacao t = criarTransacao(new BigDecimal("30.00"), 30, TipoTransacao.COMPRA);
		
		BigDecimal imposto = calculadora.calcular(t);
		
		assertEquals(BigDecimal.ZERO, imposto);		
	}

	
	@Test
	void transacaoComValorMenorQueVinteMilNaoDeveriaTerImposto() {
		Transacao t = criarTransacao(new BigDecimal("30.00"), 30, TipoTransacao.VENDA);
		
		BigDecimal imposto = calculadora.calcular(t);
		
		assertEquals(BigDecimal.ZERO, imposto);		
	}
	
	@Test
	void deveriaCalcularImpostoDeTransacaoDoTipoCompraComValorMaiorQueVinteMil() {
		Transacao t = criarTransacao(new BigDecimal("1000.00"), 21, TipoTransacao.VENDA);
		
		BigDecimal imposto = calculadora.calcular(t);
		
		assertEquals(new BigDecimal("3150.00"), imposto);		
	}
}
