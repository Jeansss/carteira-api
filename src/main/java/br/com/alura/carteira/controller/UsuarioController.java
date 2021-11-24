package br.com.alura.carteira.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.carteira.dto.TransacaoDto;
import br.com.alura.carteira.dto.TransacaoFormDto;
import br.com.alura.carteira.dto.UsuarioDto;
import br.com.alura.carteira.dto.UsuarioFormDto;
import br.com.alura.carteira.service.UsuarioService;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired //injeção de dependencia, falando aqui para o Spring dar new nessa classe, pois eu nqo quero que meu controller tenha essa preocupação pois essa classe pode ter varias dependencias e eu teria de instanciar elas todas, delegamos ao spring fazer oq for necessário para esse objeto existir. E a classe service precisa do @Service para o spring encontra la
	private UsuarioService service;
	
	@GetMapping
	public List<UsuarioDto> listar() {
		return service.listar(); 
		
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDto> cadastrar(@RequestBody @Valid UsuarioFormDto dto, 
			UriComponentsBuilder uriBuilder) {
		UsuarioDto usuarioDto = service.cadastrar(dto);

		
		URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuarioDto.getId()).toUri();
		return ResponseEntity.created(uri).body(usuarioDto);
	}

}
