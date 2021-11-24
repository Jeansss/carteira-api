package br.com.alura.carteira.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.carteira.dto.AtualizacaoTransacaoFormDto;
import br.com.alura.carteira.dto.TransacaoDetalhadaDto;
import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.modelo.TipoTransacao;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;
import br.com.alura.carteira.service.TransacaoService;

//@Controller
@RestController //aqui eh igual o controller, mas ja com a tag responseBody , entao nao preciso ficar usando a tag responseBody em todos os métodos
@RequestMapping("transacoes") //isso aqui é a uri do modelo rest, podemos ter vários controllers na aplicação
public class TransacaoController {
	
	@Autowired
	private TransacaoService service;
//	private List<Transacao> transacoes = new ArrayList<>();
//	private ModelMapper modelMapper = new ModelMapper();

	
	@GetMapping // (productes = MediaType.APPLICATION_XML_VALUE) se quisesse devolver xml
//	@ResponseBody //falando para o Spring devolver a resposta no corpo, se nao ele acharia que a string que eu to passando no retorno é uma pagina html
	public Page<TransacaoDto> listar(@PageableDefault(size = 10) Pageable paginacao, @AuthenticationPrincipal Usuario logado) { //essa @authenticationprincipal anotacao eh para avisar ao spring pegar o usuario que foi setado na sessao da request e trazer ele pra ca, ele nao esta sendo enviado como parametro por nós e sim pelo spring
//		List<TransacaoDto> transacoesDto = new ArrayList<>();
//		for(Transacao transacao : transacoes) {
//			TransacaoDto dto = new TransacaoDto();
//			dto.setTicker(transacao.getTicker());
//			dto.setPreco(transacao.getPreco());
//			dto.setQuantidade(transacao.getQuantidade());
//			dto.setTransacao(transacao.getTipoTransacao());
//			
//			transacoesDto.add(dto);
//		}
//		return transacoesDto;
//		
//		2
//		return transacoes
//				.stream()
//				.map(t -> modelMapper.map(t, TransacaoDto.class))
//				.collect(Collectors.toList());
//		3
		return service.listar(paginacao, logado);
	}
		
		
	@PostMapping
	public ResponseEntity<TransacaoDto> cadastrar(@RequestBody @Valid TransacaoFormDto dto, UriComponentsBuilder uriBuilder,  @AuthenticationPrincipal Usuario logado) { // necessário a anotação para o string saber pegar os parametros da request // anotacao valid para o spring realizar a validação das regras que eu coloquei na classe dto nos atributos, (notempty, notnull, size...)
//		Transacao transacao = new Transacao(
//				dto.getTicker(),
//				dto.getData(),
//				dto.getPreco(),
//				dto.getQuantidade(),
//				dto.getTransacao());
//		2
//		Transacao transacao = modelMapper.map(dto, Transacao.class);
//
//		
//		transacoes.add(transacao);
//		3
		TransacaoDto transacaoDto = service.cadastrar(dto, logado);
//		path("/transacoes/{id}").buildAndExpand(transacaoDto.getId())
		URI uri = uriBuilder.path("/transacoes/{id}").buildAndExpand(transacaoDto.getId()).toUri();
		return ResponseEntity.created(uri).body(transacaoDto);
	}
	
	@PutMapping
	public ResponseEntity<TransacaoDto> atualizar(@RequestBody @Valid AtualizacaoTransacaoFormDto dto, @AuthenticationPrincipal Usuario logado) { // necessário a anotação para o string saber pegar os parametros da request // anotacao valid para o spring realizar a validação das regras que eu coloquei na classe dto nos atributos, (notempty, notnull, size...)
		TransacaoDto atualizada = service.atualizar(dto, logado);
		return ResponseEntity.ok(atualizada);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<TransacaoDto> deletar(@PathVariable @NotNull Long id, @AuthenticationPrincipal Usuario logado) { //pathvariable para falar ao spring que o parametro que está vindo no método é para ser enviado no path, nao no body
		service.remover(id, logado);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TransacaoDetalhadaDto> detalhar(@PathVariable @NotNull Long id, @AuthenticationPrincipal Usuario logado) { //pathvariable para falar ao spring que o parametro que está vindo no método é para ser enviado no path, nao no body
		TransacaoDetalhadaDto dto = service.detalhar(id, logado);
		return ResponseEntity.ok(dto); //nao preciso passar o método .build, pois o metodo ok() com parametro ele ja faz o build automatico.
	}
	
	
	

}
