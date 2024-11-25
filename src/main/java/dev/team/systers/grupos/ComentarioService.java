package dev.team.systers.grupos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.usuarios.Usuario;
import dev.team.systers.grupos.Postagem;
import dev.team.systers.grupos.PostagemRepository;

@Service
public class ComentarioService {
    private final ComentarioRepository comentarioRepository;
    private final PostagemRepository postagemRepository;

    @Autowired
    public ComentarioService(ComentarioRepository comentarioRepository, PostagemRepository postagemRepository) {
        this.comentarioRepository = comentarioRepository;
        this.postagemRepository = postagemRepository;
    }

    public Comentario criarComentario(Long postagemId, String conteudo, Usuario autor) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new ComentarioException("Postagem não encontrada"));

        if (conteudo == null || conteudo.trim().isEmpty()) {
            throw new ComentarioException("O conteúdo do comentário não pode estar vazio");
        }

        Comentario comentario = new Comentario();
        comentario.setConteudo(conteudo);
        comentario.setAutor(autor);
        comentario.setPostagem(postagem);

        return comentarioRepository.save(comentario);
    }

    public void editarComentario(Long comentarioId, String novoConteudo, Usuario autor) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new ComentarioException("Comentário não encontrado"));

        if (!comentario.getAutor().equals(autor)) {
            throw new ComentarioException("Permissão negada: apenas o autor pode editar este comentário");
        }

        if (novoConteudo == null || novoConteudo.trim().isEmpty()) {
            throw new ComentarioException("O conteúdo do comentário não pode estar vazio");
        }

        comentario.setConteudo(novoConteudo);
        comentarioRepository.save(comentario);
    }

    public void apagarComentario(Long comentarioId, Usuario usuario) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new ComentarioException("Comentário não encontrado"));

        if (!comentario.getAutor().equals(usuario)) {
            throw new ComentarioException("Permissão negada: apenas o autor pode apagar este comentário");
        }

        comentarioRepository.delete(comentario);
    }

    public List<Comentario> listarComentariosPorPostagem(Long postagemId) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new ComentarioException("Postagem não encontrada"));

        return comentarioRepository.findByPostagem(postagem);
    }
}
