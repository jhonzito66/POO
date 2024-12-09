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

@Service
public class MembroService {
    private final MembroRepository membroRepository;
    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public MembroService(MembroRepository membroRepository, GrupoRepository grupoRepository, UsuarioRepository usuarioRepository) {
        this.membroRepository = membroRepository;
        this.grupoRepository = grupoRepository;
        this.usuarioRepository = usuarioRepository;
    }

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

    public List<Membro> listarMembrosPorGrupo(Long grupoId) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new MembroException("Grupo não encontrado"));

        return membroRepository.findByGrupo(grupo);
    }

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

    public Membro buscarMembroPorUsuarioEGrupo(Long usuarioId, Long grupoId) {
        return membroRepository.findByUsuarioIdAndGrupoId(usuarioId, grupoId);
    }
}
