package dev.team.systers.repository;

import java.util.List;

import dev.team.systers.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.team.systers.model.Postagem;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByPostagem(Postagem postagem);
}
