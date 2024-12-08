package dev.team.systers.repository;

import dev.team.systers.model.Grupo;
import dev.team.systers.model.Membro;
import dev.team.systers.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    List<Grupo> findAllByMembros(Membro membro);

    List<Grupo> findAllByMembros_Usuario(Usuario usuario);

}
