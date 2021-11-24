package br.com.alura.carteira.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.alura.carteira.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByLogin(String login); //com o optional , o objeto pode ou nao devolver um usuario, e esse método que queremos é uma busca pelo login

	
	@Query("SELECT u FROM Usuario u JOIN FETCH u.perfis WHERE u.id = :id")
	Optional<Usuario> carregaPorIdComPerfis(Long id);

}
