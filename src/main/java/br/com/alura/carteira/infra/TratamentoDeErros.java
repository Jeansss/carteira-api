package br.com.alura.carteira.infra;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.alura.carteira.dto.Error400Dto;
import br.com.alura.carteira.dto.Error500Dto;

//@ControllerAdvice // para o spring conhecer essa classe e configurar ela como uma classe que vai fazer tratamento nos controllers
@RestControllerAdvice // igual a de cima para aplicações rest
public class TratamentoDeErros {
	
	@ExceptionHandler(MethodArgumentNotValidException.class) //diz para o spring que esse método é para tratar // Quando acontece erro do bean validation, o spring lança uma exception chamada , MethodArgumentNotValid
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) // para dizer ao spring para retornar o status code de bad request quue é 400
	public List<Error400Dto> tratarErro404(MethodArgumentNotValidException ex) {
		return ex
				.getFieldErrors()
				.stream()
				.map(erro -> new Error400Dto(erro.getField(), erro.getDefaultMessage()))
				.collect(Collectors.toList());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Error500Dto tratarErro500(Exception ex, HttpServletRequest req) {
		return new Error500Dto(
				LocalDateTime.now(),
				ex.getClass().toString(),
				ex.getMessage(),
				req.getRequestURI());
	}			
	
	@ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class})
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public void tratarErro404() {

	}	
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public String tratarErro403(AccessDeniedException e) {
		return e.getMessage();
	}		


}
