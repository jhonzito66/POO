package dev.team.systers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Grupo;

/**
 * Repositório para operações de persistência de Grupo.
 * Fornece métodos para acessar e manipular dados de grupos no banco de dados.
 */
@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    /**
     * Busca todos os membros de um grupo específico.
     * Utiliza SQL nativo para otimizar a consulta de relacionamentos.
     * 
     * @param grupoId ID do grupo para buscar os membros
     * @return Lista de IDs dos usuários membros do grupo
     */
    @Query(value = "SELECT usuario_id " +
            "FROM usuario " +
            "JOIN membro ON usuario_id = membro_id " +
            "WHERE grupo_id_membro_fk = :grupoId",
            nativeQuery = true)
    List<Object[]> findMembrosDeUmGrupoNativo(@Param("grupoId") Long grupoId);

    /**
     * Busca um grupo pelo seu nome exato.
     * 
     * @param nome Nome do grupo
     * @return Grupo encontrado ou vazio se não existir
     */
    Optional<Grupo> findByNome(String nome);

    /**
     * Busca grupos que contenham determinado texto na descrição.
     * Realiza busca case-insensitive com correspondência parcial.
     * 
     * @param descricao Texto a ser buscado na descrição
     * @return Lista de grupos que correspondem ao critério
     */
    @Query("SELECT g FROM Grupo g WHERE LOWER(g.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
    List<Grupo> findGruposByDescricao(@Param("descricao") String descricao);

    /**
     * Busca grupos com quantidade de membros superior ao especificado.
     * Utiliza a função SIZE do JPQL para contar membros.
     * 
     * @param quantidade Número mínimo de membros
     * @return Lista de grupos com mais membros que o especificado
     */
    @Query("SELECT g FROM Grupo g WHERE SIZE(g.membros) > :quantidade")
    List<Grupo> findGruposComMaisDeMembros(@Param("quantidade") int quantidade);

    /**
     * Busca um grupo pelo seu ID.
     * 
     * @param id ID do grupo
     * @return Grupo encontrado ou null se não existir
     */
    Grupo findGrupoById(Long id);

    /**
     * Busca grupos que contenham o texto no nome, ordenados por número de membros.
     * Realiza busca case-insensitive com correspondência parcial.
     * 
     * @param nome Texto a ser buscado no nome
     * @return Lista de grupos ordenada por quantidade de membros (decrescente)
     */
    List<Grupo> findByNomeContainingIgnoreCaseOrderByMembrosDesc(String nome);

    /**
     * Lista todos os grupos ordenados por número de membros.
     * 
     * @return Lista de grupos ordenada por quantidade de membros (decrescente)
     */
    List<Grupo> findAllByOrderByMembrosDesc();
}

