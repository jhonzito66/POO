package dev.team.systers.controller;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import dev.team.systers.exception.GrupoException;
import dev.team.systers.exception.UsuarioException;
import dev.team.systers.model.Grupo;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.GrupoService;
import dev.team.systers.service.UsuarioService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class GrupoController {
    private final GrupoService grupoService;
    private final UsuarioService usuarioService;

    @Autowired
    public GrupoController(GrupoService grupoService, UsuarioService usuarioService) {
        this.grupoService = grupoService;
        this.usuarioService = usuarioService;
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

    @GetMapping("/grupos/detalhes/{nome}")
    public String exibirDetalhesGrupo(@PathVariable String nome, Model model) {
        // Buscar o grupo pelo nome
        Grupo grupo = grupoService.buscarGrupoPorNome(nome);
        if (grupo == null) {
            throw new IllegalArgumentException("Grupo não encontrado com o nome: " + nome);
        }

        // Buscar o usuário associado ao grupo
        Usuario usuario = (Usuario) grupo.getUsuario(); // Certifique-se de que `getUsuario` está implementado corretamente
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário associado ao grupo não encontrado");
        }

        // Adicionar dados ao modelo para a view
        model.addAttribute("grupo", grupo);
        model.addAttribute("usuario", usuario);

        return "detalhes";
    }

}
