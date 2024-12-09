package dev.team.systers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dev.team.systers.model.Grupo;
import dev.team.systers.model.Membro;
import dev.team.systers.model.Postagem;

/**
 * Repositório para operações de persistência de Postagem.
 * Fornece métodos para acessar e manipular dados de postagens em grupos.
 */
@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> {
    
    /**
     * Busca postagens de um grupo específico.
     * @param grupo Grupo para buscar as postagens
     * @return Lista de postagens do grupo
     */
    List<Postagem> findByGrupo(Grupo grupo);

    /**
     * Busca todas as postagens de um grupo.
     * Método alternativo para busca de postagens.
     * @param grupo Grupo para buscar as postagens
     * @return Lista de postagens do grupo
     */
    List<Postagem> getAllByGrupo(Grupo grupo);

    /**
     * Busca postagens de um grupo ordenadas por data de criação.
     * @param grupo Grupo para buscar as postagens
     * @return Lista de postagens ordenada por data (mais recentes primeiro)
     */
    List<Postagem> findByGrupoOrderByDataCriacaoDesc(Grupo grupo);

    /**
     * Exclui todas as postagens de um membro específico.
     * @param autor Membro autor das postagens a serem excluídas
     */
    @Modifying
    @Transactional
    void deleteByAutor(Membro autor);
}
