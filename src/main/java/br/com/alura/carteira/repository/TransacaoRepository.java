package br.com.alura.carteira.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.alura.carteira.dto.ItemCarteiraDto;
import br.com.alura.carteira.modelo.Transacao;
import br.com.alura.carteira.modelo.Usuario;

//nao preciso de uma anotacao repository pois o sprint consegue encontrar interfaces sem anotcao, só classes que necessitam
// o Spring que dinamicamente vai gerar uma  classe que implementar essa interface automaticamente
public interface TransacaoRepository extends JpaRepository<Transacao, Long>{

	
//	@Query("select new br.com.alura.carteira.dto.ItemCarteiraDto(" //necessário passsar para a jpa umnew na classe dto que é o que eu vou retornar nesse select
//			+ "t.ticker, "
//			+ "sum(t.quantidade), "
//			+ "round(sum(t.quantidade) * 1.0 / (select sum(t2.quantidade) from Transacao t2) * 1.0 , 3)) " //colocamos aqui o quantidade * 1.0 pois ele converte o quantidade que é integer para double, se nao ele ia ficar achando que é Long no banco de dados
//			+ "from Transacao t "  //aqui eu to dando um from mas nao é da tabela transacoes, é da entidade Transacao
//			+ "group by t.ticker")
	
	@Query("select new br.com.alura.carteira.dto.ItemCarteiraDto(" //necessário passsar para a jpa umnew na classe dto que é o que eu vou retornar nesse select
			+ "t.ticker, "
			+ "sum(CASE WHEN(t.tipo = 'COMPRA') THEN t.quantidade ELSE (t.quantidade * -1) END), "
			+ "(select sum(CASE WHEN(t2.tipo = 'COMPRA') THEN t2.quantidade ELSE (t2.quantidade * -1) END) from Transacao t2)) " //colocamos aqui o quantidade * 1.0 pois ele converte o quantidade que é integer para double, se nao ele ia ficar achando que é Long no banco de dados
			+ "from Transacao t "  //aqui eu to dando um from mas nao é da tabela transacoes, é da entidade Transacao
			+ "group by t.ticker")
	List<ItemCarteiraDto> relatorioCarteiraDeInvestimentos(); //implementar essa interfce do spring que ja tem toda a logicaa de persistencia de uma classe dao, dai passo como generics parametros a entidade que vou trabalhar e o tipo do Id que é a chave primaria de cada registro

	Page<Transacao> findAllByUsuario(Pageable paginacao, Usuario usuario);

}
