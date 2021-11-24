package br.com.alura.carteira.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.modelo.TipoTransacao;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.repository.TransacaoRepository;
import br.com.alura.carteira.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class) //para o Junit usar a extensão do Mockito para ela ler os tributos da classe de teste e criar os atributos e classes pra mock
class TransacaoServiceTest {
	
	@Mock //falando que esse cara aqui deve ser um mock
	private TransacaoRepository repository;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Mock
	private ModelMapper modelMapper;
	
	private Usuario logado;
	
	@BeforeEach
	public void before() {
		this.logado = new Usuario("Rodrigo", "rodrigo@gmail.com", "123456");
	}
	
	@InjectMocks // para injetar os atributos mockados acima dentro dessa classe service, como se fosse ela mesma
	private TransacaoService service;
	
	private TransacaoFormDto criarTransacao() {
		TransacaoFormDto formDto = new TransacaoFormDto(
				"ITSA4",
				new BigDecimal("10.45"),
				LocalDate.now(),
				120,
				TipoTransacao.COMPRA,
				1l
			);
		return formDto;
	}

	@Test
	void deveriaCadastrarUmaTransacao() {
		
		TransacaoFormDto formDto = criarTransacao();
		
		Mockito.when(usuarioRepository.getById(formDto.getUsuarioId()))
		.thenReturn(logado); //dizendo ao mockito que quando o usuarioRepository chamar o getById, entao eh pre retornar o usuario logado
		
		Transacao transacao = new Transacao(formDto.getTicker(), 
				formDto.getData(), formDto.getPreco(), 120, TipoTransacao.COMPRA, logado);
		
		Mockito.when(modelMapper.map(formDto, Transacao.class))
		.thenReturn(transacao);
		
		Mockito.when(modelMapper.map(transacao, TransacaoDto.class))
		.thenReturn(new TransacaoDto(
				null,
				transacao.getTicker(),
				transacao.getPreco(),
				transacao.getQuantidade(),
				transacao.getTipo()
				));
		
		
		TransacaoDto transacaoDto = service.cadastrar(formDto, logado);
		
		Mockito.verify(repository).save(Mockito.any()); //para verificar se o metodo save do repository foi chamado com qualquer parametro, por isso o Mockito.any
		
		assertEquals(formDto.getTicker(), transacaoDto.getTicker());
		assertEquals(formDto.getPreco(), transacaoDto.getPreco());
		assertEquals(formDto.getQuantidade(), transacaoDto.getQuantidade());
		assertEquals(formDto.getTipo(), transacaoDto.getTipo());


	}

	
	@Test
	void naoDeveriaCadastrarUmaTransacaoComUsuarioInexistente() {
		
		TransacaoFormDto formDto = criarTransacao();
		
		Mockito
		.when(usuarioRepository.getById(formDto.getUsuarioId()))
		.thenThrow(EntityNotFoundException.class);
				
		assertThrows(IllegalArgumentException.class, () -> service.cadastrar(formDto, logado));


	}

}
