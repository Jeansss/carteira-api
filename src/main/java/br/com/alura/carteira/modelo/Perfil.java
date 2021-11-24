package br.com.alura.carteira.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id") //o equalsHashCode aqui é baseado no id, entao vai considerar que dois usuarios sao iguais se o id for igual, e estou aplicando isso com o lombok, é uma boa praticar ter em todas as entidades o equalshashcode baseado no id
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "perfis")
public class Perfil  implements GrantedAuthority{ //necessario implementar interface para o spring entender que q essa classe tbm representa um perfil

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;

	@Override
	public String getAuthority() {
		return this.nome;  //apenas um metodo para retornar qual o nome do perfil
	}
}
