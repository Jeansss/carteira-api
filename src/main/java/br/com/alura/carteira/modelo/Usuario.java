package br.com.alura.carteira.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.alura.carteira.dto.UsuarioDto;
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
@Table(name = "usuarios")
public class Usuario implements UserDetails { //necessário implementar a interface UserDetails, pois o método loadUserByUsername espera que retorno um UserDetails e está atualmente retornando um Usuário
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String login;
	private String senha;
	
	@ManyToMany //muitos para muitos, muitos perfis para muitos usuarios ou muitos usuarios para mtos perfis
	@JoinTable(name = "perfis_usuarios",
			joinColumns = @JoinColumn(name = "usuario_id"),
			inverseJoinColumns = @JoinColumn(name = "perfil_id"))// quando temos relacionamento muitos para muitos surge uma tabela intermediário, que seria uma tabela de join, que é uma nova tabela que vai passar a fazer essa nova associacao, usuario x tem o perfil y, usuario x tem o perfil 2
	private List<Perfil> perfis = new ArrayList<>(); //pq vai haver um relacionamento do user com a tabela de perfis agora, e um user pode ter mais de um perfil
	
	public Usuario (String nome, String login, String senha) {
		this.nome = nome;
		this.login = login;
		this.senha = senha;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.perfis;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return login;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public void adicionarPerfil(Perfil perfil) {
		this.perfis.add(perfil);
		
	}


}
