package br.com.alura.carteira.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import br.com.alura.carteira.modelo.TipoTransacao;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoDto {
	
	private Long id;
	private String ticker;
	private BigDecimal preco;
	private Integer quantidade;
	private TipoTransacao tipo;
	private BigDecimal imposto;

}
