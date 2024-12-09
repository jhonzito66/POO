package dev.team.systers.service;

import java.time.LocalDateTime;
import java.util.List;

import dev.team.systers.exception.ComentarioException;
import dev.team.systers.model.Comentario;
import dev.team.systers.repository.ComentarioRepository;
import dev.team.systers.repository.PostagemRepository;
import dev.team.systers.repository.MembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.model.Usuario;
import dev.team.systers.model.Postagem;
import dev.team.systers.model.Membro;

@Service
public class ComentarioService {
    private final ComentarioRepository comentarioRepository;
    private final PostagemRepository postagemRepository;
    private final MembroRepository membroRepository;

    @Autowired
    public ComentarioService(ComentarioRepository comentarioRepository,
                           PostagemRepository postagemRepository,
                           MembroRepository membroRepository) {
        this.comentarioRepository = comentarioRepository;
        this.postagemRepository = postagemRepository;
        this.membroRepository = membroRepository;
    }

    public Comentario criarComentario(Long postagemId, String conteudo, Usuario autor) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new ComentarioException("Postagem não encontrada"));

        Membro membro = membroRepository.findByUsuarioAndGrupo(autor, postagem.getGrupo())
                .orElseThrow(() -> new ComentarioException("Usuário não é membro do grupo"));

        Comentario comentario = new Comentario();
        comentario.setConteudo(conteudo);
        comentario.setAutor(membro.getUsuario());
        comentario.setPostagem(postagem);
        comentario.setDataCriacao(LocalDateTime.now());

        return comentarioRepository.save(comentario);
    }

    public void excluirComentario(Long comentarioId, Usuario usuario) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new ComentarioException("Comentário não encontrado"));

        Membro membro = membroRepository.findByUsuarioAndGrupo(usuario, comentario.getPostagem().getGrupo())
                .orElseThrow(() -> new ComentarioException("Usuário não é membro do grupo"));

        // Verifica se o usuário é o autor do comentário ou tem permissão para excluir
        if (!comentario.getAutor().equals(membro) && 
            membro.getAutorizacao() != Membro.Autorizacao.DONO && 
            membro.getAutorizacao() != Membro.Autorizacao.MODERADOR) {
            throw new ComentarioException("Permissão negada: apenas o autor, moderadores ou dono podem excluir este comentário");
        }

        comentarioRepository.delete(comentario);
    }

    public List<Comentario> listarComentariosPorPostagem(Long postagemId) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new ComentarioException("Postagem não encontrada"));

        return comentarioRepository.findByPostagem(postagem);
    }
}
