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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.team.systers.exception.GrupoException;
import dev.team.systers.exception.UsuarioException;
import dev.team.systers.model.Grupo;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.GrupoService;
import dev.team.systers.service.UsuarioService;


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

    @GetMapping("/grupos/{id}")
    public String exibirGrupo(@PathVariable String id, Model model) {
        // Buscar o grupo pelo id
        Grupo grupo = grupoService.buscarGrupoPorNome(id);
        if (grupo == null) {
            throw new IllegalArgumentException("Grupo não encontrado com o id: " + id);
        }

        // Buscar o usuário associado ao grupo
        Usuario usuario = (Usuario) grupo.getUsuario(); // Certifique-se de que `getUsuario` está implementado corretamente
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário associado ao grupo não encontrado");
        }

        // Adicionar dados ao modelo para a view
        model.addAttribute("grupo", grupo);
        model.addAttribute("usuario", usuario);

        return "grupo";
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
}
