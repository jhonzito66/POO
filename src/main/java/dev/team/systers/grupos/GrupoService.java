package dev.team.systers.grupos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.usuarios.Usuario;
import dev.team.systers.usuarios.UsuarioRepository;

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
        Grupo grupo = new Grupo();
        grupo.setNome(nome);
        grupo.setDescricao(descricao);
        grupo.setStatusAtivo(true);

        grupoRepository.save(grupo);

        Membro membro = new Membro();
        membro.setTag(criador.getLogin());
        membro.setNome(criador.getNome());
        membro.setAutorizacao(Membro.Autorizacao.DONO);
        membro.setStatusAcesso(Membro.StatusAcesso.NORMAL);
        membro.setUsuario(criador);
        membro.setGrupo(grupo);

        membroRepository.save(membro);

        return grupo;
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

    public void apagarPostagem(Long postagemId, Usuario usuario) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new GrupoException("Postagem não encontrada"));

        Membro autor = membroRepository.findByUsuarioAndGrupo(postagem.getAutor().getUsuario(), postagem.getGrupo())
                .orElseThrow(() -> new GrupoException("Usuário não é membro do grupo"));

        if (!autor.getUsuario().equals(usuario) && autor.getAutorizacao() != Membro.Autorizacao.MODERADOR) {
            throw new GrupoException("Permissão negada: apenas o autor ou um moderador pode apagar esta postagem");
        }

        postagemRepository.delete(postagem);
    }

    private void verificarPermissao(Usuario usuario, Grupo grupo, Membro.Autorizacao autorizacaoNecessaria) {
        Membro membro = membroRepository.findByUsuarioAndGrupo(usuario, grupo)
                .orElseThrow(() -> new GrupoException("Usuário não é membro do grupo"));
        if (membro.getAutorizacao().ordinal() < autorizacaoNecessaria.ordinal()) {
            throw new GrupoException("Permissão negada");
        }
    }
}
