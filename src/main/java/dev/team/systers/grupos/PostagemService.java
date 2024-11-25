package dev.team.systers.grupos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.usuarios.Usuario;

@Service
public class PostagemService {
    private final PostagemRepository postagemRepository;
    private final GrupoRepository grupoRepository;
    private final MembroRepository membroRepository;

    @Autowired
    public PostagemService(PostagemRepository postagemRepository, GrupoRepository grupoRepository, MembroRepository membroRepository) {
        this.postagemRepository = postagemRepository;
        this.grupoRepository = grupoRepository;
        this.membroRepository = membroRepository;
    }

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

    public List<Postagem> listarPostagensPorGrupo(Long grupoId) {
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new PostagemException("Grupo não encontrado"));

        return postagemRepository.findByGrupo(grupo);
    }
}
