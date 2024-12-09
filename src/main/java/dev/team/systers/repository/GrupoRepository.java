package dev.team.systers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Grupo;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    /**
     * Encontra todos os membros de um grupo específico pelo ID do grupo.
     * @param grupoId
     * @return Lista de membros do grupo.
     */
    @Query(value = "SELECT usuario_id " +
            "FROM usuario " +
            "JOIN membro ON usuario_id = membro_id " +
            "WHERE grupo_id_membro_fk = :grupoId",
            nativeQuery = true)
    List<Object[]> findMembrosDeUmGrupoNativo(@Param("grupoId") Long grupoId);

    /**
     * Encontra um grupo pelo nome.
     * @param nome
     * @return Grupo com o nome especificado.
     */
    Optional<Grupo> findByNome(String nome);

    /**
     * Encontra grupos com base em um critério de descrição parcial.
     * @param descricao
     * @return Lista de grupos.
     */
    @Query("SELECT g FROM Grupo g WHERE LOWER(g.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
    List<Grupo> findGruposByDescricao(@Param("descricao") String descricao);

    /**
     * Busca grupos que possuem mais de um determinado número de membros.
     * @param quantidade
     * @return Lista de grupos.
     */
    @Query("SELECT g FROM Grupo g WHERE SIZE(g.membros) > :quantidade")
    List<Grupo> findGruposComMaisDeMembros(@Param("quantidade") int quantidade);

    Grupo findGrupoById(Long id);
}

