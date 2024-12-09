package dev.team.systers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dev.team.systers.model.Comentario;
import dev.team.systers.model.Membro;
import dev.team.systers.model.Postagem;

/**
 * Repositório para operações de persistência de Comentário.
 * Fornece métodos para acessar e manipular dados de comentários em postagens.
 */
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    
    /**
     * Busca comentários de uma postagem específica.
     * @param postagem Postagem para buscar os comentários
     * @return Lista de comentários da postagem
     */
    List<Comentario> findByPostagem(Postagem postagem);

    /**
     * Busca comentários de uma postagem ordenados por data.
     * @param postagem Postagem para buscar os comentários
     * @return Lista de comentários ordenada por data (mais antigos primeiro)
     */
    List<Comentario> findByPostagemOrderByDataCriacaoAsc(Postagem postagem);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comentario c WHERE c.autor = :membro")
    void deleteByAutor(@Param("membro") Membro membro);
}
