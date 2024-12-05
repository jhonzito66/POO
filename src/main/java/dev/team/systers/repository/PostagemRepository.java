package dev.team.systers.repository;

import java.util.List;

import dev.team.systers.model.Grupo;
import dev.team.systers.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> {
    List<Postagem> findByGrupo(Grupo grupo);

    List<Postagem> getAllByGrupo(Grupo grupo);
}
