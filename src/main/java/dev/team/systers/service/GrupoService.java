package dev.team.systers.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.team.systers.exception.GrupoException;
import dev.team.systers.exception.MembroException;
import dev.team.systers.model.Grupo;
import dev.team.systers.model.Membro;
import dev.team.systers.model.Postagem;
import dev.team.systers.model.Usuario;
import dev.team.systers.repository.ComentarioRepository;
import dev.team.systers.repository.GrupoRepository;
import dev.team.systers.repository.MembroRepository;
import dev.team.systers.repository.PostagemRepository;
import dev.team.systers.repository.UsuarioRepository;

/**
 * Serviço responsável pelo gerenciamento de grupos no sistema.
 * Fornece funcionalidades para criar, modificar e gerenciar grupos,
 * incluindo operações de membros, postagens e moderação.
 */
@Service
public class GrupoService {
    
    /**
     * Repositório para acesso aos dados de grupos.
     */
    private final GrupoRepository grupoRepository;

    /**
     * Repositório para acesso aos dados de membros.
     */
    private final MembroRepository membroRepository;

    /**
     * Repositório para acesso aos dados de usuários.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Repositório para acesso aos dados de postagens.
     */
    private final PostagemRepository postagemRepository;

    /**
     * Repositório para acesso aos dados de comentários.
     */
    private final ComentarioRepository comentarioRepository;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     * @param grupoRepository Repositório de grupos
     * @param membroRepository Repositório de membros
     * @param usuarioRepository Repositório de usuários
     * @param postagemRepository Repositório de postagens
     * @param comentarioRepository Repositório de comentários
     */
    @Autowired
    public GrupoService(GrupoRepository grupoRepository, MembroRepository membroRepository, 
                       UsuarioRepository usuarioRepository, PostagemRepository postagemRepository, 
                       ComentarioRepository comentarioRepository) {
        this.grupoRepository = grupoRepository;
        this.membroRepository = membroRepository;
        this.usuarioRepository = usuarioRepository;
        this.postagemRepository = postagemRepository;
        this.comentarioRepository = comentarioRepository;
    }

    /**
     * Cria um novo grupo com o usuário criador como dono.
     * @param nome Nome do grupo
     * @param descricao Descrição do grupo
     * @param criador Usuário que está criando o grupo
     * @throws MembroException se houver erro ao criar o membro
     * @throws GrupoException se houver erro ao criar o grupo
     */
    public void criarGrupo(String nome, String descricao, Usuario criador) {
        try {
            Membro membro = new Membro();
            membro.setTag(criador.getLogin());
            membro.setNome(criador.getNome());
            membro.setAutorizacao(Membro.Autorizacao.DONO);
            membro.setStatusAcesso(Membro.StatusAcesso.NORMAL);
            membro.setUsuario(criador);

            Grupo grupo = new Grupo();
            grupo.setNome(nome);
            grupo.setDescricao(descricao);
            grupo.setStatusAtivo(true);

            grupoRepository.save(grupo);
            membro.setGrupo(grupo);
            
            membroRepository.save(membro);

        } catch (MembroException e) {
            throw new MembroException(e.getMessage());
        } catch (GrupoException e) {
            throw new GrupoException(e.getMessage());
        }
    }

    /**
     * Adiciona um usuário como membro de um grupo.
     * @param grupoId ID do grupo
     * @param usuarioId ID do usuário
     * @throws GrupoException se o grupo não existir, usuário não for encontrado ou já for membro
     */
    public void participarGrupo(Long grupoId, Long usuarioId) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new GrupoException("Usuário não encontrado"));

        boolean jaMembro = membroRepository.findByUsuarioAndGrupo(usuario, grupo).isPresent();
        if (jaMembro) {
            throw new GrupoException("Você já é membro deste grupo");
        }

        Membro membro = new Membro();
        membro.setTag(usuario.getLogin());
        membro.setNome(usuario.getNome());
        membro.setAutorizacao(Membro.Autorizacao.PADRAO);
        membro.setStatusAcesso(Membro.StatusAcesso.NORMAL);
        membro.setUsuario(usuario);
        membro.setGrupo(grupo);

        membroRepository.save(membro);
    }

    /**
     * Lista todos os grupos do sistema.
     * @return Lista de todos os grupos
     */
    public List<Grupo> listarGrupos() {
        return grupoRepository.findAll();
    }

    /**
     * Altera o nome de um grupo.
     * @param grupoId ID do grupo
     * @param novoNome Novo nome do grupo
     * @param usuario Usuário que está realizando a alteração
     * @throws GrupoException se o grupo não existir ou usuário não tiver permissão
     */
    public void renomearGrupo(Long grupoId, String novoNome, Usuario usuario) {
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        verificarPermissao(usuario, grupo, Membro.Autorizacao.DONO);
        grupo.setNome(novoNome);
        grupoRepository.save(grupo);
    }

    /**
     * Altera a descrição de um grupo.
     * @param grupoId ID do grupo
     * @param novaDescricao Nova descrição do grupo
     * @param usuario Usuário que está realizando a alteração
     * @throws GrupoException se o grupo não existir ou usuário não tiver permissão
     */
    public void alterarDescricaoGrupo(Long grupoId, String novaDescricao, Usuario usuario) {
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        verificarPermissao(usuario, grupo, Membro.Autorizacao.DONO);
        grupo.setDescricao(novaDescricao);
        grupoRepository.save(grupo);
    }

    /**
     * Altera o status de acessibilidade de um grupo.
     * @param grupoId ID do grupo
     * @param aberto Status de acessibilidade (true para aberto, false para fechado)
     * @param usuario Usuário que está realizando a alteração
     * @throws GrupoException se o grupo não existir ou usuário não tiver permissão
     */
    public void gerenciarAcessibilidadeGrupo(Long grupoId, boolean aberto, Usuario usuario) {
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        verificarPermissao(usuario, grupo, Membro.Autorizacao.DONO);
        grupo.setStatusAtivo(aberto);
        grupoRepository.save(grupo);
    }

    /**
     * Exclui um grupo e todo seu conteúdo.
     * @param grupoId ID do grupo
     * @param usuario Usuário que está realizando a exclusão
     * @throws GrupoException se o grupo não existir ou usuário não tiver permissão
     */
    public void excluirGrupo(Long grupoId, Usuario usuario) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        
        verificarPermissao(usuario, grupo, Membro.Autorizacao.DONO);
        
        List<Postagem> postagens = postagemRepository.getAllByGrupo(grupo);
        for (Postagem postagem : postagens) {
            comentarioRepository.deleteAll(postagem.getComentarios());
        }
        
        postagemRepository.deleteAll(postagens);
        membroRepository.deleteAll(grupo.getMembros());
        grupoRepository.delete(grupo);
    }

    /**
     * Altera o status de acesso de um membro do grupo.
     * @param grupoId ID do grupo
     * @param membroId ID do membro
     * @param status Novo status de acesso
     * @param usuario Usuário que está realizando a moderação
     * @throws GrupoException se o grupo/membro não existir ou usuário não tiver permissão
     */
    public void moderarMembro(Long grupoId, Long membroId, Membro.StatusAcesso status, Usuario usuario) {
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        verificarPermissao(usuario, grupo, Membro.Autorizacao.MODERADOR);
        Membro membro = membroRepository.findById(membroId).orElseThrow(() -> new GrupoException("Membro não encontrado"));
        membro.setStatusAcesso(status);
        membroRepository.save(membro);
    }

    /**
     * Lista todas as postagens de um grupo.
     * @param grupo Grupo para listar as postagens
     * @return Lista de postagens do grupo
     */
    private List<Postagem> listarPostagens(Grupo grupo) {
        return postagemRepository.getAllByGrupo(grupo);
    }

    /**
     * Verifica se um usuário tem a autorização necessária em um grupo.
     * @param usuario Usuário a ser verificado
     * @param grupo Grupo em questão
     * @param autorizacaoNecessaria Nível de autorização necessário
     * @throws GrupoException se o usuário não tiver a autorização necessária
     */
    private void verificarPermissao(Usuario usuario, Grupo grupo, Membro.Autorizacao autorizacaoNecessaria) {
        Membro membro = membroRepository.findByUsuarioAndGrupo(usuario, grupo)
                .orElseThrow(() -> new GrupoException("Usuário não é membro do grupo"));
        if (membro.getAutorizacao().ordinal() < autorizacaoNecessaria.ordinal()) {
            throw new GrupoException("Permissão negada");
        }
    }

    /**
     * Lista todos os grupos dos quais um usuário é membro.
     * @param usuario Usuário para listar os grupos
     * @return Lista de grupos do usuário
     */
    public List<Grupo> listarGruposPorUsuario(Usuario usuario) {
        return membroRepository.findByUsuario(usuario)
                .stream()
                .map(Membro::getGrupo)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Lista todos os membros de um grupo.
     * @param grupoId ID do grupo
     * @return Lista de membros do grupo
     * @throws GrupoException se o grupo não existir
     */
    public List<Membro> visualizarMembros(Long grupoId) {
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        return grupo.getMembros();
    }

    /**
     * Lista todas as postagens de um grupo.
     * @param grupoId ID do grupo
     * @return Lista de postagens do grupo
     * @throws GrupoException se o grupo não existir
     */
    public List<Postagem> visualizarPostagens(Long grupoId) {
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        return listarPostagens(grupo);
    }

    /**
     * Remove um usuário de um grupo.
     * @param grupoId ID do grupo
     * @param usuarioId ID do usuário
     * @throws GrupoException se o grupo não existir, usuário não for membro ou for o dono
     */
    @Transactional
    public void deixarGrupo(Long grupoId, Long usuarioId) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new GrupoException("Usuário não encontrado"));

        Membro membro = membroRepository.findByUsuarioAndGrupo(usuario, grupo)
                .orElseThrow(() -> new GrupoException("Usuário não é membro do grupo"));

        if (membro.getAutorizacao() == Membro.Autorizacao.DONO) {
            throw new GrupoException("O dono do grupo não pode sair sem transferir a posse.");
        }

        // Primeiro, exclui todos os comentários feitos pelo membro
        comentarioRepository.deleteByAutor(membro);

        // Depois, exclui todas as postagens feitas pelo membro
        postagemRepository.deleteByAutor(membro);

        // Por fim, remove o membro do grupo
        membroRepository.delete(membro);
    }

    /**
     * Busca um grupo pelo nome exato.
     * @param nome Nome do grupo
     * @return Grupo encontrado
     * @throws IllegalArgumentException se o grupo não existir
     */
    public Grupo buscarGrupoPorNome(String nome) {
        return grupoRepository.findByNome(nome)
                .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado com o nome: " + nome));
    }

    /**
     * Busca um grupo pelo ID.
     * @param id ID do grupo
     * @return Grupo encontrado ou null se não existir
     */
    public Grupo buscarGrupoPorId(Long id) {
        return grupoRepository.findGrupoById(id);
    }

    /**
     * Busca grupos que contenham o texto no nome.
     * @param nome Texto a ser buscado no nome
     * @return Lista de grupos ordenada por número de membros
     */
    public List<Grupo> buscarGruposPorNome(String nome) {
        return grupoRepository.findByNomeContainingIgnoreCaseOrderByMembrosDesc(nome);
    }

    /**
     * Lista todos os grupos ordenados por número de membros.
     * @return Lista de grupos ordenada por número de membros (decrescente)
     */
    public List<Grupo> listarTodosGruposOrdenadosPorMembros() {
        return grupoRepository.findAllByOrderByMembrosDesc();
    }
}
