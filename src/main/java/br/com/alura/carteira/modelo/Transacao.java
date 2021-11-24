package br.com.alura.carteira.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter 
@Setter
@EqualsAndHashCode(of = "id") //o equalsHashCode aqui é baseado no id, entao vai considerar que dois usuarios sao iguais se o id for igual, e estou aplicando isso com o lombok, é uma boa praticar ter em todas as entidades o equalshashcode baseado no id
@ToString(exclude = {"data", "quantidade"})
@AllArgsConstructor // criar construtor com todos os atributos
@NoArgsConstructor // cria construtor padrao
@Entity //Pra falar que é uma uma entidade que é uma classe que esta mapeando uma tabela do banco
@Table(name = "transacoes") // para indicar que o nome da tabela será transacoes, se nao por default ele pegaria o nome da classe
public class Transacao {
	
	// tenho que ter esse id para representar pk
	  @Id //para dizer ao jpa que ele é a minha pk
	  @GeneratedValue(strategy = GenerationType.IDENTITY) //para dizer a gpa que quem vai gerar e gerenciar esse id nao é aplicacao e sim o banco de dados, para dizer como o valor é gerado e o atributo que passo é qual a estrategia de geracao dos ids, no nosso caso é o auto increment.
	  private Long id;
//	  @Column(name = "tck") caso eu queira mudar o nome de alguma coluna
	  private String ticker;
	  private LocalDate data;
	  private BigDecimal preco;
	  private Integer quantidade; //boa prática usar os wrappers e nao os tipos primitivos tipo int, pois se nao nao teria como pegar o valor null, seria o valor default q é 0
	  
	  @Enumerated(EnumType.STRING) //para falar que esse tipo é uma strin para o banco ai lá el transforma em varchar, se nao ele ia pegar o padrao ordinal que seria int
	  private TipoTransacao tipo;
	  
//	  @JoinColumn(name = "id_usuario") //para mudar o nome da coluna, e essa é uma anotacao somente para colunas que sao de relacionamento com outras tabelas.  se eu nao quisesse mudar o nome da coluna ela seria usuario_id, pois ele pega o nome do atributo e concatena com o primeiro atributo da classe usuario
	  @ManyToOne //para o jpa saber que existe um relacionamento aqui, ja que a classe Usuario tbm é uma entidade, e essa notação é muitos para um, pois poderão haver muitras transacoes para um usuario.
	  private Usuario usuario;

//	  public Transacao(){}

//	  @Override
//	  public String toString() {
//
//	    return "Transacao{" +
//	        "ticker='" + ticker + '\'' +
//	        ", data=" + data +
//	        ", preco=" + preco +
//	        ", quantidade=" + quantidade +
//	        ", tipoTransacao=" + tipoTransacao +
//	        '}';
//	  }

	  public Transacao(String ticker, LocalDate data, BigDecimal preco, int quantidade,
	      TipoTransacao tipoTransacao, Usuario usuario) {

	    this.ticker = ticker;
	    this.data = data;
	    this.preco = preco;
	    this.quantidade = quantidade;
	    this.tipo = tipoTransacao;
	    this.usuario = usuario;
	  }

	public void atualizarInformacoes(String ticker, LocalDate data, BigDecimal preco, int quantidade,
			TipoTransacao tipo) {
		
		this.ticker = ticker;
		this.data = data;
		this.preco = preco;
		this.quantidade = quantidade;
		this.tipo = tipo;
		
	}
}
