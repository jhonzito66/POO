package dev.team.systers.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.exception.MembroException;
import dev.team.systers.model.Grupo;
import dev.team.systers.model.Membro;
import dev.team.systers.model.Usuario;
import dev.team.systers.repository.GrupoRepository;
import dev.team.systers.repository.MembroRepository;
import dev.team.systers.repository.UsuarioRepository;

/**
 * Serviço responsável pelo gerenciamento de membros em grupos.
 * Fornece funcionalidades para adicionar, remover e gerenciar membros,
 * incluindo controle de permissões e autorizações.
 */
@Service
public class MembroService {
    
    /**
     * Repositório para acesso aos dados de membros.
     */
    private final MembroRepository membroRepository;

    /**
     * Repositório para acesso aos dados de grupos.
     */
    private final GrupoRepository grupoRepository;

    /**
     * Repositório para acesso aos dados de usuários.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     * @param membroRepository Repositório de membros
     * @param grupoRepository Repositório de grupos
     * @param usuarioRepository Repositório de usuários
     */
    @Autowired
    public MembroService(MembroRepository membroRepository, GrupoRepository grupoRepository, UsuarioRepository usuarioRepository) {
        this.membroRepository = membroRepository;
        this.grupoRepository = grupoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Adiciona um novo membro a um grupo.
     * 
     * @param grupoId ID do grupo
     * @param usuarioId ID do usuário a ser adicionado
     * @param autorizacao Nível de autorização do membro
     * @return Membro criado
     * @throws MembroException se o grupo/usuário não existir ou usuário já for membro
     */
    public Membro adicionarMembro(Long grupoId, Long usuarioId, Membro.Autorizacao autorizacao) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new MembroException("Grupo não encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new MembroException("Usuário não encontrado"));

        if (membroRepository.existsByUsuarioAndGrupo(usuario, grupo)) {
            throw new MembroException("Usuário já é membro do grupo");
        }

        Membro membro = new Membro();
        membro.setUsuario(usuario);
        membro.setGrupo(grupo);
        membro.setTag(usuario.getLogin());
        membro.setNome(usuario.getNome());
        membro.setAutorizacao(autorizacao != null ? autorizacao : Membro.Autorizacao.PADRAO);
        membro.setStatusAcesso(Membro.StatusAcesso.NORMAL);

        return membroRepository.save(membro);
    }

    /**
     * Remove um membro de um grupo.
     * Apenas moderadores podem remover membros.
     * 
     * @param grupoId ID do grupo
     * @param usuarioId ID do usuário a ser removido
     * @param solicitante Usuário que está solicitando a remoção
     * @throws MembroException se o grupo/usuário não existir ou solicitante não tiver permissão
     */
    public void removerMembro(Long grupoId, Long usuarioId, Usuario solicitante) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new MembroException("Grupo não encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new MembroException("Usuário não encontrado"));

        Membro solicitanteMembro = membroRepository.findByUsuarioAndGrupo(solicitante, grupo)
                .orElseThrow(() -> new MembroException("Usuário não é membro do grupo"));

        Membro membro = membroRepository.findByUsuarioAndGrupo(usuario, grupo)
                .orElseThrow(() -> new MembroException("Usuário não é membro do grupo"));

        if (solicitanteMembro.getAutorizacao().ordinal() < Membro.Autorizacao.MODERADOR.ordinal()) {
            throw new MembroException("Permissão negada: apenas moderadores podem remover membros");
        }

        membroRepository.delete(membro);
    }

    /**
     * Lista todos os membros de um grupo.
     * 
     * @param grupoId ID do grupo
     * @return Lista de membros do grupo
     * @throws MembroException se o grupo não existir
     */
    public List<Membro> listarMembrosPorGrupo(Long grupoId) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new MembroException("Grupo não encontrado"));

        return membroRepository.findByGrupo(grupo);
    }

    /**
     * Altera o nível de permissão de um membro.
     * Apenas o dono do grupo pode alterar permissões.
     * 
     * @param grupoId ID do grupo
     * @param usuarioId ID do usuário a ter permissão alterada
     * @param novaAutorizacao Nova autorização a ser atribuída
     * @param solicitante Usuário que está solicitando a alteração
     * @throws MembroException se o grupo/usuário não existir ou solicitante não tiver permissão
     */
    public void alterarPermissaoMembro(Long grupoId, Long usuarioId, Membro.Autorizacao novaAutorizacao, Usuario solicitante) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new MembroException("Grupo não encontrado"));

        Membro solicitanteMembro = membroRepository.findByUsuarioAndGrupo(solicitante, grupo)
                .orElseThrow(() -> new MembroException("Usuário não é membro do grupo"));

        if (solicitanteMembro.getAutorizacao() != Membro.Autorizacao.DONO) {
            throw new MembroException("Permissão negada: apenas o dono pode alterar permissões");
        }

        Membro membro = membroRepository.findByUsuarioAndGrupo(usuarioRepository.findById(usuarioId)
                        .orElseThrow(() -> new MembroException("Usuário não encontrado")), grupo)
                .orElseThrow(() -> new MembroException("Usuário não é membro do grupo"));

        membro.setAutorizacao(novaAutorizacao);
        membroRepository.save(membro);
    }

    /**
     * Busca um membro específico pela combinação de usuário e grupo.
     * 
     * @param usuarioId ID do usuário
     * @param grupoId ID do grupo
     * @return Membro encontrado ou null se não existir
     */
    public Membro buscarMembroPorUsuarioEGrupo(Long usuarioId, Long grupoId) {
        return membroRepository.findByUsuarioIdAndGrupoId(usuarioId, grupoId);
    }
}
