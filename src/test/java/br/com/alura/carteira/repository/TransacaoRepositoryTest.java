package br.com.alura.carteira.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.alura.carteira.dto.ItemCarteiraDto;
import br.com.alura.carteira.modelo.TipoTransacao;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;



@ExtendWith(SpringExtension.class) //para o juni extender essa classe
@DataJpaTest //para o spring carregar as coisas do datajpa no nosso projeto, ou seja trás tudo necessário para lidar com banco de dados
@AutoConfigureTestDatabase(replace = Replace.NONE) //ta falando para o spring nao substitui meu banco de dados pelo banco de dados em memória
@ActiveProfiles("test") //ele vai procurar o arquivo application-test para usar as informacoes dele aqui, que é o profile que deve estar ativo para os testes
class TransacaoRepositoryTest {
	
	@Autowired
	private TransacaoRepository repository;
	
	@Autowired
	private TestEntityManager em;

	@Test
	void deveriaRetornarRelatorioDeCarteiraInvestimento() {
		
		Usuario usuario = new Usuario("Jean", "jean@test.com", "nova1010");
		em.persist(usuario);
		
		Transacao t1 = new Transacao("ITSA4",
				LocalDate.now(),
				new BigDecimal("10.00"),
				50,
				TipoTransacao.COMPRA,
				usuario);
		
		em.persist(t1);
		
		Transacao t2 = new Transacao("EGIE3",
				LocalDate.now(),
				new BigDecimal("30.00"),
				25,
				TipoTransacao.COMPRA,
				usuario);
		
		em.persist(t2);
		
		Transacao t3 = new Transacao("ELK41",
				LocalDate.now(),
				new BigDecimal("35.00"),
				88,
				TipoTransacao.COMPRA,
				usuario);
		
		em.persist(t3);
		
		Transacao t4 = new Transacao("MANU2",
				LocalDate.now(),
				new BigDecimal("60.00"),
				10,
				TipoTransacao.COMPRA,
				usuario);
		
		em.persist(t4);
		
		Transacao t5 = new Transacao("BRU91",
				LocalDate.now(),
				new BigDecimal("10.00"),
				40,
				TipoTransacao.COMPRA,
				usuario);
		
		em.persist(t5);
		
		List<ItemCarteiraDto> relatorio = repository.relatorioCarteiraDeInvestimentos();
		
		Assertions
		.assertThat(relatorio)
		.hasSize(5)
		.extracting(ItemCarteiraDto::getTicker, ItemCarteiraDto::getQuantidade, ItemCarteiraDto::getPercentual)
		.containsExactlyInAnyOrder( //contém exatamente isso, nao importa a ordem que retornou
				Assertions.tuple("ITSA4", 50l, new BigDecimal("23.47")),
				Assertions.tuple("EGIE3", 25l, new BigDecimal("11.74")),
				Assertions.tuple("ELK41", 88l, new BigDecimal("41.31")),
				Assertions.tuple("MANU2", 10l, new BigDecimal("4.69")),
				Assertions.tuple("BRU91", 40l, new BigDecimal("18.78"))
				);
	}
	
	@Test
	void deveriaRetornarRelatorioDeCarteiraInvestimentoLevandoEmContaAsVendas() {
		
		Usuario usuario = new Usuario("Jean", "jean@test.com", "nova1010");
		em.persist(usuario);
		
		Transacao t1 = new Transacao("ITSA4",
				LocalDate.now(),
				new BigDecimal("10.00"),
				50,
				TipoTransacao.COMPRA,
				usuario);
		
		em.persist(t1);
		
		Transacao t2 = new Transacao("ITSA4",
				LocalDate.now(),
				new BigDecimal("30.00"),
				40,
				TipoTransacao.VENDA,
				usuario);
		
		em.persist(t2);
		
		Transacao t3 = new Transacao("ELK41",
				LocalDate.now(),
				new BigDecimal("35.00"),
				88,
				TipoTransacao.COMPRA,
				usuario);
		
		em.persist(t3);
		
		Transacao t4 = new Transacao("MANU2",
				LocalDate.now(),
				new BigDecimal("60.00"),
				10,
				TipoTransacao.COMPRA,
				usuario);
		
		em.persist(t4);
		
		Transacao t5 = new Transacao("BRU91",
				LocalDate.now(),
				new BigDecimal("10.00"),
				40,
				TipoTransacao.COMPRA,
				usuario);
		
		em.persist(t5);
		
		List<ItemCarteiraDto> relatorio = repository.relatorioCarteiraDeInvestimentos();
		
		Assertions
		.assertThat(relatorio)
		.hasSize(4)
		.extracting(ItemCarteiraDto::getTicker, ItemCarteiraDto::getQuantidade, ItemCarteiraDto::getPercentual)
		.containsExactlyInAnyOrder( //contém exatamente isso, nao importa a ordem que retornou
				Assertions.tuple("ITSA4", 10l, new BigDecimal("6.76")),
				Assertions.tuple("ELK41", 88l, new BigDecimal("59.46")),
				Assertions.tuple("MANU2", 10l, new BigDecimal("6.76")),
				Assertions.tuple("BRU91", 40l, new BigDecimal("27.03"))
				);
	}

}


