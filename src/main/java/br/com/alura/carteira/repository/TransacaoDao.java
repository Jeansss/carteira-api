package br.com.alura.carteira.repository;

import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import br.com.alura.carteira.modelo.Transacao;


@Repository //para essa classe ficar visivel para o spring, e aqui é como um repositório onde teremos acesso aos dados da aplicação, sem isso o autowired nao ia funcionar pois o spring nao conseguiria enxergar essa classe
public class TransacaoDao {
	
	@Autowired
	private EntityManager em;
	
	public void cadastrar(Transacao transacao) {
		em.persist(transacao);

	}

	public List<Transacao> listar() {
		return em
				.createQuery("select t from Transacao t", Transacao.class) //aqui colocamos uma consulta para o banco de dados na linhaguem jpql que é uma linguagem do jpa que é cmo se fosse um sql Orientado a objetos. ao invés de passar o nome da tabela do bacno eu passo o nome da Entidade que é Transacao., e no segundo parametro eu passo a classe da entidade que vai ser devolvida nessa query
				.getResultList(); // E o getResultList é o método para disparar a query
			}

}
