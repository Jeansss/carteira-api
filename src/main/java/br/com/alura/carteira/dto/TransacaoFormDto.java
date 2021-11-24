package br.com.alura.carteira.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import br.com.alura.carteira.modelo.TipoTransacao;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoFormDto {
	
//	@NotNull
//	@NotEmpty
	@NotBlank
	@Size(min = 5, max = 6)
	@Pattern(regexp = "[a-zA-Z]{4}[0-9][0-9]?", message = "{transacao.ticker.invalido}") //na message está uma chave que eu criei no arquivo de ValidationMessage
	private String ticker;
	
	@DecimalMin("0.01")
	private BigDecimal preco;
	
	@PastOrPresent
	private LocalDate data;
	
	@NotNull
	private int quantidade;
	
	@NotNull
	private TipoTransacao tipo;
	
	@JsonAlias("usuario_id") //para identificar esse campo como usuario_id no postman
	private Long usuarioId;
	

}
