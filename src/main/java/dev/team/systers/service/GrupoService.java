package dev.team.systers.service;

import java.util.ArrayList;
import java.util.List;

import dev.team.systers.exception.GrupoException;
import dev.team.systers.exception.MembroException;
import dev.team.systers.model.Grupo;
import dev.team.systers.model.Membro;
import dev.team.systers.model.Postagem;
import dev.team.systers.repository.GrupoRepository;
import dev.team.systers.repository.MembroRepository;
import dev.team.systers.repository.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.model.Usuario;
import dev.team.systers.repository.UsuarioRepository;

@Service
public class GrupoService {
    private final GrupoRepository grupoRepository;
    private final MembroRepository membroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PostagemRepository postagemRepository;

    @Autowired
    public GrupoService(GrupoRepository grupoRepository, MembroRepository membroRepository, UsuarioRepository usuarioRepository, PostagemRepository postagemRepository) {
        this.grupoRepository = grupoRepository;
        this.membroRepository = membroRepository;
        this.usuarioRepository = usuarioRepository;
        this.postagemRepository = postagemRepository;
    }

    public Grupo criarGrupo(String nome, String descricao, Usuario criador) {
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

            return grupo;
        } catch (MembroException e) {
            throw new MembroException(e.getMessage());
        } catch (GrupoException e) {
            throw new GrupoException(e.getMessage());
        }
    }

    public void participarGrupo(Long grupoId, Long usuarioId) {
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new GrupoException("Usuário não encontrado"));

        Membro membro = new Membro();
        membro.setTag(usuario.getLogin());
        membro.setNome(usuario.getNome());
        membro.setAutorizacao(Membro.Autorizacao.PADRAO);
        membro.setStatusAcesso(Membro.StatusAcesso.NORMAL);
        membro.setUsuario(usuario);
        membro.setGrupo(grupo);

        membroRepository.save(membro);
    }

    public List<Grupo> listarGrupos() {
        return grupoRepository.findAll();
    }

    public void renomearGrupo(Long grupoId, String novoNome, Usuario usuario) {
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        verificarPermissao(usuario, grupo, Membro.Autorizacao.DONO);
        grupo.setNome(novoNome);
        grupoRepository.save(grupo);
    }

    public void alterarDescricaoGrupo(Long grupoId, String novaDescricao, Usuario usuario) {
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        verificarPermissao(usuario, grupo, Membro.Autorizacao.DONO);
        grupo.setDescricao(novaDescricao);
        grupoRepository.save(grupo);
    }

    public void gerenciarAcessibilidadeGrupo(Long grupoId, boolean aberto, Usuario usuario) {
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        verificarPermissao(usuario, grupo, Membro.Autorizacao.DONO);
        grupo.setStatusAtivo(aberto);
        grupoRepository.save(grupo);
    }

    public void excluirGrupo(Long grupoId, Usuario usuario) {
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        verificarPermissao(usuario, grupo, Membro.Autorizacao.DONO);
        grupoRepository.delete(grupo);
    }

    public void moderarMembro(Long grupoId, Long membroId, Membro.StatusAcesso status, Usuario usuario) {
        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoException("Grupo não encontrado"));
        verificarPermissao(usuario, grupo, Membro.Autorizacao.MODERADOR);
        Membro membro = membroRepository.findById(membroId).orElseThrow(() -> new GrupoException("Membro não encontrado"));
        membro.setStatusAcesso(status);
        membroRepository.save(membro);
    }

    private List<Postagem> listarPostagens(Grupo grupo) {
        return postagemRepository.getAllByGrupo(grupo);
    }

    private void verificarPermissao(Usuario usuario, Grupo grupo, Membro.Autorizacao autorizacaoNecessaria) {
        Membro membro = membroRepository.findByUsuarioAndGrupo(usuario, grupo)
                .orElseThrow(() -> new GrupoException("Usuário não é membro do grupo"));
        if (membro.getAutorizacao().ordinal() < autorizacaoNecessaria.ordinal()) {
            throw new GrupoException("Permissão negada");
        }
    }
}
