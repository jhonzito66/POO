package dev.team.systers.repository;

import dev.team.systers.model.Avaliacao;
import dev.team.systers.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

}
