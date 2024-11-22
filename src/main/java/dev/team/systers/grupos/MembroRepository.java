package dev.team.systers.grupos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.team.systers.usuarios.Usuario;

@Repository
public interface MembroRepository extends JpaRepository<Membro, Long>{
    Optional<Membro> findByUsuarioAndGrupo(Usuario usuario, Grupo grupo);
}
