package dev.team.systers.grupos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.team.systers.grupos.Postagem;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByPostagem(Postagem postagem);
}
