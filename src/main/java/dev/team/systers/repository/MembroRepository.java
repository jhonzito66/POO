package dev.team.systers.repository;

import java.util.List;
import java.util.Optional;

import dev.team.systers.model.Grupo;
import dev.team.systers.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Usuario;

@Repository
public interface MembroRepository extends JpaRepository<Membro, Long>{
    Optional<Membro> findByUsuarioAndGrupo(Usuario usuario, Grupo grupo);

    boolean existsByUsuarioAndGrupo(Usuario usuario, Grupo grupo);

    List<Membro> findByGrupo(Grupo grupo);
}
