package br.com.alura.carteira.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.alura.carteira.dto.ItemCarteiraDto;
import br.com.alura.carteira.modelo.Perfil;
import br.com.alura.carteira.modelo.Transacao;

public interface PerfilRepository extends JpaRepository<Perfil, Long>{


}
