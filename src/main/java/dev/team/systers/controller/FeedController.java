package dev.team.systers.controller;

import dev.team.systers.model.Postagem;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.PostagemService;
import dev.team.systers.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FeedController {
    private final PostagemService postagemService;
    private final UsuarioService usuarioService;

    @Autowired
    public FeedController(PostagemService postagemService, UsuarioService usuarioService) {
        this.postagemService = postagemService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/feed")
    public String exibirFeed(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        Usuario usuario = usuarioService.encontrarPorLogin(login);
        
        // Carrega as últimas 10 postagens dos grupos do usuário
        List<Postagem> postagens = postagemService.listarUltimas10PostagensDeTodosOsGruposDoUsuario(usuario);
        model.addAttribute("postagens", postagens);
        
        // Carrega usuários denunciados se for administrador
        if (usuario.getAutorizacao() == Usuario.Autorizacao.ADMINISTRADOR) {
            List<Usuario> usuariosDenunciados = usuarioService.listarUsuariosDenunciados();
            model.addAttribute("usuariosDenunciados", usuariosDenunciados);
        }
        
        model.addAttribute("usuario", usuario);
        return "feed";
    }
}
