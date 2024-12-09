package dev.team.systers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.team.systers.exception.GrupoException;
import dev.team.systers.exception.UsuarioException;
import dev.team.systers.model.Grupo;
import dev.team.systers.model.Membro;
import dev.team.systers.model.Postagem;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.ComentarioService;
import dev.team.systers.service.GrupoService;
import dev.team.systers.service.MembroService;
import dev.team.systers.service.PostagemService;
import dev.team.systers.service.UsuarioService;


@Controller
public class GrupoController {
    private final GrupoService grupoService;
    private final UsuarioService usuarioService;
    private final PostagemService postagemService;
    private final ComentarioService comentarioService;
    private final MembroService membroService;

    @Autowired
    public GrupoController(GrupoService grupoService, UsuarioService usuarioService, PostagemService postagemService, ComentarioService comentarioService, MembroService membroService) {
        this.grupoService = grupoService;
        this.usuarioService = usuarioService;
        this.postagemService = postagemService;
        this.comentarioService = comentarioService;
        this.membroService = membroService;
    }

    @GetMapping("/grupos")
    public String exibirGrupos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("title", "grupos");
        model.addAttribute("content", "grupos");
        model.addAttribute("sidebar", "perfil-template");

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getName().equals("anonymousUser")) {
            throw new UsuarioException("Usuário não autenticado");
        }

        String username = auth.getName();
        Usuario usuario = usuarioService.encontrarPorLogin(username);
        if (usuario == null) {
            throw new UsuarioException("Usuário não encontrado");
        }
        model.addAttribute("usuario", usuario);

        List<Grupo> grupos = grupoService.listarGruposPorUsuario(usuario);
        model.addAttribute("grupos", grupos);

        return "grupos";
    }

    @GetMapping("/grupos/criar-grupo")
    public String registrarGrupo(Model model) {
        model.addAttribute("grupo", new Grupo());
        return "/criar-grupo";
    }

    @PostMapping("/grupos/criar-grupo")
    public String registrarGrupo(@ModelAttribute("grupo") Grupo grupo, Model model) {
        model.addAttribute("grupo", new Grupo());

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null ||
                    !auth.isAuthenticated() ||
                    auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            String username = auth.getName();
            Usuario usuario = usuarioService.encontrarPorLogin(username);

            if (usuario == null) {
                throw new UsuarioException("Usuário não encontrado");
            }
            model.addAttribute("usuario", usuario);

            grupoService.criarGrupo(grupo.getNome(), grupo.getDescricao(), usuario);
        } catch (GrupoException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            return "/grupos/criar-grupo";
        }
        return "redirect:/grupos";
    }

    @GetMapping("/grupos/grupo/{id}")
    public String exibirGrupo(@PathVariable Long id, Model model) {
        try {
            // Obter usuário autenticado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            String username = auth.getName();
            Usuario usuarioAutenticado = usuarioService.encontrarPorLogin(username);
            if (usuarioAutenticado == null) {
                throw new UsuarioException("Usuário não encontrado");
            }

            // Buscar o grupo pelo id
            Grupo grupo = grupoService.buscarGrupoPorId(id);
            if (grupo == null) {
                throw new GrupoException("Grupo não encontrado");
            }

            // Verificar se o usuário é membro do grupo
            Membro membroAtual = membroService.buscarMembroPorUsuarioEGrupo(usuarioAutenticado.getId(), grupo.getId());
            if (membroAtual == null) {
                throw new GrupoException("Você não é membro deste grupo");
            }

            // Buscar postagens do grupo
            List<Postagem> postagens = postagemService.listarPostagensPorGrupo(grupo.getId());

            // Adicionar dados ao modelo
            model.addAttribute("grupo", grupo);
            model.addAttribute("membro", membroAtual);
            model.addAttribute("postagens", postagens);
            model.addAttribute("novaPostagem", new Postagem());
            
            return "grupo";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", e.getMessage());
            return "redirect:/grupos";
        }
    }

    @PostMapping("/grupos/excluir/{id}")
    public String excluirGrupo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Obter o usuário autenticado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            // Buscar o usuário autenticado
            String username = auth.getName();
            Usuario usuarioAutenticado = usuarioService.encontrarPorLogin(username);
            if (usuarioAutenticado == null) {
                throw new UsuarioException("Usuário não encontrado");
            }

            // Excluir o grupo
            grupoService.excluirGrupo(id, usuarioAutenticado);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Grupo excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir grupo: " + e.getMessage());
        }

        return "redirect:/grupos";
    }

    // Criar nova postagem
    @PostMapping("/grupos/grupo/{id}/postagem")
    public String criarPostagem(@PathVariable Long id, 
                              @RequestParam String conteudo, 
                              RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            Usuario usuario = usuarioService.encontrarPorLogin(auth.getName());
            if (usuario == null) {
                throw new UsuarioException("Usuário não encontrado");
            }

            postagemService.criarPostagem(id, conteudo, usuario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Postagem criada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao criar postagem: " + e.getMessage());
        }
        return "redirect:/grupos/grupo/" + id;
    }

    // Excluir postagem
    @PostMapping("/grupos/grupo/{grupoId}/postagem/{postagemId}/excluir")
    public String excluirPostagem(@PathVariable Long grupoId,
                                @PathVariable Long postagemId,
                                RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            Usuario usuario = usuarioService.encontrarPorLogin(auth.getName());
            if (usuario == null) {
                throw new UsuarioException("Usuário não encontrado");
            }

            postagemService.excluirPostagem(postagemId, usuario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Postagem excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir postagem: " + e.getMessage());
        }
        return "redirect:/grupos/grupo/" + grupoId;
    }

    // Criar novo comentário
    @PostMapping("/grupos/grupo/{grupoId}/postagem/{postagemId}/comentario")
    public String criarComentario(@PathVariable Long grupoId,
                                @PathVariable Long postagemId,
                                @RequestParam String conteudo,
                                RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            Usuario usuario = usuarioService.encontrarPorLogin(auth.getName());
            if (usuario == null) {
                throw new UsuarioException("Usuário não encontrado");
            }

            comentarioService.criarComentario(postagemId, conteudo, usuario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Comentário adicionado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao adicionar comentário: " + e.getMessage());
        }
        return "redirect:/grupos/grupo/" + grupoId;
    }

    // Excluir comentário
    @PostMapping("/grupos/grupo/{grupoId}/comentario/{comentarioId}/excluir")
    public String excluirComentario(@PathVariable Long grupoId,
                                  @PathVariable Long comentarioId,
                                  RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            Usuario usuario = usuarioService.encontrarPorLogin(auth.getName());
            if (usuario == null) {
                throw new UsuarioException("Usuário não encontrado");
            }

            comentarioService.excluirComentario(comentarioId, usuario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Comentário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir comentário: " + e.getMessage());
        }
        return "redirect:/grupos/grupo/" + grupoId;
    }
}
