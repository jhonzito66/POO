package dev.team.systers.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.exception.PostagemException;
import dev.team.systers.model.Grupo;
import dev.team.systers.model.Membro;
import dev.team.systers.model.Postagem;
import dev.team.systers.model.Usuario;
import dev.team.systers.repository.GrupoRepository;
import dev.team.systers.repository.MembroRepository;
import dev.team.systers.repository.PostagemRepository;

/**
 * Serviço responsável pelo gerenciamento de postagens em grupos.
 * Fornece funcionalidades para criar, editar, excluir e listar postagens,
 * incluindo verificações de permissões e validações.
 */
@Service
public class PostagemService {
    
    /**
     * Repositório para acesso aos dados de postagens.
     */
    private final PostagemRepository postagemRepository;

    /**
     * Repositório para acesso aos dados de grupos.
     */
    private final GrupoRepository grupoRepository;

    /**
     * Repositório para acesso aos dados de membros.
     */
    private final MembroRepository membroRepository;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     * @param postagemRepository Repositório de postagens
     * @param grupoRepository Repositório de grupos
     * @param membroRepository Repositório de membros
     */
    @Autowired
    public PostagemService(PostagemRepository postagemRepository, GrupoRepository grupoRepository, MembroRepository membroRepository) {
        this.postagemRepository = postagemRepository;
        this.grupoRepository = grupoRepository;
        this.membroRepository = membroRepository;
    }

    /**
     * Cria uma nova postagem em um grupo.
     * Verifica se o usuário é membro do grupo antes de permitir a criação.
     * 
     * @param grupoId ID do grupo onde a postagem será criada
     * @param conteudo Texto da postagem
     * @param autor Usuário que está criando a postagem
     * @return Postagem criada
     * @throws PostagemException se o grupo não existir, usuário não for membro ou conteúdo for vazio
     */
    public Postagem criarPostagem(Long grupoId, String conteudo, Usuario autor) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new PostagemException("Grupo não encontrado"));

        Membro membro = membroRepository.findByUsuarioAndGrupo(autor, grupo)
                .orElseThrow(() -> new PostagemException("Usuário não é membro do grupo"));

        if (conteudo == null || conteudo.trim().isEmpty()) {
            throw new PostagemException("O conteúdo da postagem não pode estar vazio");
        }

        Postagem postagem = new Postagem();
        postagem.setConteudo(conteudo);
        postagem.setAutor(membro);
        postagem.setGrupo(grupo);

        return postagemRepository.save(postagem);
    }

    /**
     * Edita uma postagem existente.
     * Apenas o autor pode editar a postagem.
     * 
     * @param postagemId ID da postagem a ser editada
     * @param novoConteudo Novo texto da postagem
     * @param autor Usuário que está tentando editar
     * @throws PostagemException se a postagem não existir, usuário não for o autor ou conteúdo for vazio
     */
    public void editarPostagem(Long postagemId, String novoConteudo, Usuario autor) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new PostagemException("Postagem não encontrada"));

        if (!postagem.getAutor().getUsuario().equals(autor)) {
            throw new PostagemException("Permissão negada: apenas o autor pode editar esta postagem");
        }

        if (novoConteudo == null || novoConteudo.trim().isEmpty()) {
            throw new PostagemException("O conteúdo da postagem não pode estar vazio");
        }

        postagem.setConteudo(novoConteudo);
        postagemRepository.save(postagem);
    }

    /**
     * Apaga uma postagem existente.
     * Apenas o autor, moderadores ou dono do grupo podem apagar.
     * 
     * @param postagemId ID da postagem a ser apagada
     * @param solicitante Usuário que está tentando apagar
     * @throws PostagemException se a postagem não existir ou usuário não tiver permissão
     */
    public void apagarPostagem(Long postagemId, Usuario solicitante) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new PostagemException("Postagem não encontrada"));

        Membro solicitanteMembro = membroRepository.findByUsuarioAndGrupo(solicitante, postagem.getGrupo())
                .orElseThrow(() -> new PostagemException("Usuário não é membro do grupo"));

        if (!solicitante.equals(postagem.getAutor().getUsuario())
                && solicitanteMembro.getAutorizacao() != Membro.Autorizacao.MODERADOR
                && solicitanteMembro.getAutorizacao() != Membro.Autorizacao.DONO) {
            throw new PostagemException("Permissão negada: apenas o autor ou moderadores podem apagar esta postagem");
        }

        postagemRepository.delete(postagem);
    }

    /**
     * Lista todas as postagens de um grupo, ordenadas por data de criação.
     * 
     * @param grupoId ID do grupo
     * @return Lista de postagens do grupo
     * @throws PostagemException se o grupo não existir
     */
    public List<Postagem> listarPostagensPorGrupo(Long grupoId) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new PostagemException("Grupo não encontrado"));
        return postagemRepository.findByGrupoOrderByDataCriacaoDesc(grupo);
    }

    /**
     * Lista as 10 postagens mais recentes de todos os grupos do usuário.
     * 
     * @param usuario Usuário para listar as postagens
     * @return Lista das últimas 10 postagens dos grupos do usuário
     */
    public List<Postagem> listarUltimas10PostagensDeTodosOsGruposDoUsuario(Usuario usuario) {
        if (usuario == null || usuario.getMembros() == null || usuario.getMembros().isEmpty()) {
            return Collections.emptyList();
        }

        return usuario.getMembros().stream()
                .map(Membro::getGrupo)
                .flatMap(grupo -> postagemRepository.getAllByGrupo(grupo).stream())
                .sorted(Comparator.comparing(Postagem::getDataCriacao).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Exclui uma postagem existente.
     * Apenas o autor, moderadores ou dono do grupo podem excluir.
     * 
     * @param postagemId ID da postagem a ser excluída
     * @param usuario Usuário que está tentando excluir
     * @throws PostagemException se a postagem não existir ou usuário não tiver permissão
     */
    public void excluirPostagem(Long postagemId, Usuario usuario) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new PostagemException("Postagem não encontrada"));

        Membro membro = membroRepository.findByUsuarioAndGrupo(usuario, postagem.getGrupo())
                .orElseThrow(() -> new PostagemException("Usuário não é membro do grupo"));

        if (!postagem.getAutor().equals(membro) && 
            membro.getAutorizacao() != Membro.Autorizacao.DONO && 
            membro.getAutorizacao() != Membro.Autorizacao.MODERADOR) {
            throw new PostagemException("Permissão negada: apenas o autor, moderadores ou dono podem excluir esta postagem");
        }

        postagemRepository.delete(postagem);
    }
}
