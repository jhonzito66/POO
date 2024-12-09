package dev.team.systers.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.exception.ComentarioException;
import dev.team.systers.model.Comentario;
import dev.team.systers.model.Membro;
import dev.team.systers.model.Postagem;
import dev.team.systers.model.Usuario;
import dev.team.systers.repository.ComentarioRepository;
import dev.team.systers.repository.MembroRepository;
import dev.team.systers.repository.PostagemRepository;

/**
 * Serviço responsável pelo gerenciamento de comentários em postagens.
 * Fornece funcionalidades para criar, excluir e listar comentários,
 * incluindo verificações de permissões e validações.
 */
@Service
public class ComentarioService {
    
    /**
     * Repositório para acesso aos dados de comentários.
     */
    private final ComentarioRepository comentarioRepository;

    /**
     * Repositório para acesso aos dados de postagens.
     */
    private final PostagemRepository postagemRepository;

    /**
     * Repositório para acesso aos dados de membros.
     */
    private final MembroRepository membroRepository;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     * @param comentarioRepository Repositório de comentários
     * @param postagemRepository Repositório de postagens
     * @param membroRepository Repositório de membros
     */
    @Autowired
    public ComentarioService(ComentarioRepository comentarioRepository,
                           PostagemRepository postagemRepository,
                           MembroRepository membroRepository) {
        this.comentarioRepository = comentarioRepository;
        this.postagemRepository = postagemRepository;
        this.membroRepository = membroRepository;
    }

    /**
     * Cria um novo comentário em uma postagem.
     * Verifica se o usuário é membro do grupo antes de permitir o comentário.
     * 
     * @param postagemId ID da postagem a ser comentada
     * @param conteudo Texto do comentário
     * @param autor Usuário que está criando o comentário
     * @return Comentário criado
     * @throws ComentarioException se a postagem não existir ou usuário não for membro do grupo
     */
    public Comentario criarComentario(Long postagemId, String conteudo, Usuario autor) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new ComentarioException("Postagem não encontrada"));

        Membro membro = membroRepository.findByUsuarioAndGrupo(autor, postagem.getGrupo())
                .orElseThrow(() -> new ComentarioException("Usuário não é membro do grupo"));

        Comentario comentario = new Comentario();
        comentario.setConteudo(conteudo);
        comentario.setAutor(membro);
        comentario.setPostagem(postagem);
        comentario.setDataCriacao(LocalDateTime.now());

        return comentarioRepository.save(comentario);
    }

    /**
     * Exclui um comentário existente.
     * Apenas o autor do comentário, moderadores ou dono do grupo podem excluir.
     * 
     * @param comentarioId ID do comentário a ser excluído
     * @param usuario Usuário que está tentando excluir o comentário
     * @throws ComentarioException se o comentário não existir ou usuário não tiver permissão
     */
    public void excluirComentario(Long comentarioId, Usuario usuario) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new ComentarioException("Comentário não encontrado"));

        Membro membro = membroRepository.findByUsuarioAndGrupo(usuario, comentario.getPostagem().getGrupo())
                .orElseThrow(() -> new ComentarioException("Usuário não é membro do grupo"));

        if (!comentario.getAutor().equals(membro) && 
            membro.getAutorizacao() != Membro.Autorizacao.DONO && 
            membro.getAutorizacao() != Membro.Autorizacao.MODERADOR) {
            throw new ComentarioException("Permissão negada: apenas o autor, moderadores ou dono podem excluir este comentário");
        }

        comentarioRepository.delete(comentario);
    }

    /**
     * Lista todos os comentários de uma postagem.
     * 
     * @param postagemId ID da postagem
     * @return Lista de comentários da postagem
     * @throws ComentarioException se a postagem não existir
     */
    public List<Comentario> listarComentariosPorPostagem(Long postagemId) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new ComentarioException("Postagem não encontrada"));

        return comentarioRepository.findByPostagem(postagem);
    }
}
