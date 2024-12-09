package dev.team.systers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Comentario;
import dev.team.systers.model.Postagem;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByPostagem(Postagem postagem);
    List<Comentario> findByPostagemOrderByDataCriacaoAsc(Postagem postagem);
}
