package dev.team.systers.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /**
     * Encontra todos os grupos aos quais um usuário pertence.
     * @return
     */
    @Query(value = "SELECT grupo_id " +
            "FROM grupo " +
            "JOIN membro ON grupo_id = grupo_id_membro_fk " +
            "JOIN usuario ON membro_id = :usuarioId",
            nativeQuery = true)
    List<Object[]> findGruposDeUmUsuarioNativo(@Param("usuarioId") Long usuarioId);

    Optional<Usuario> findByLogin(String login);
}
