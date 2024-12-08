package dev.team.systers.controller;

import dev.team.systers.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    private final UsuarioService usuarioService;

    @Autowired
    public RootController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/feed")
    public String feed(Model model) {
        PerfilController.Authentication(model, usuarioService);
        return "feed";
    }

    @GetMapping("/")
    public String redirecionarParaOutroHTTP() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getName().equals("anonymousUser")) {
            return "redirect:/login";
        } else {
            return "redirect:/feed";
        }
    }
}