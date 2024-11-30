package dev.team.systers.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PerfilController {
    private final PerfilService perfilService;
    private final UsuarioService usuarioService;

    @Autowired
    public PerfilController(PerfilService perfilService, UsuarioService usuarioService) {
        this.perfilService = perfilService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/perfil/me")
    public String exibirPerfilAtual(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getName().equals("anonymousUser")) {
            throw new IllegalStateException("Usuário não autenticado");
        }

        String login = auth.getName();
        Usuario usuario = usuarioService.findByLogin(login);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @GetMapping("/perfil/{login}")
    public String exibirPerfilUsuario(@PathVariable String login, Model model) {
        Usuario usuario = usuarioService.findByLogin(login);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        model.addAttribute("usuario", usuario);
        return "perfil";
    }
}
