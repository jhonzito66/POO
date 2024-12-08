package dev.team.systers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.team.systers.model.Postagem;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.PostagemService;
import dev.team.systers.service.UsuarioService;

@RestController
@RequestMapping("/api/postagens")
public class PostagemController {

    private final PostagemService postagemService;
    private final UsuarioService usuarioService;

    @Autowired
    public PostagemController(PostagemService postagemService, UsuarioService usuarioService) {
        this.postagemService = postagemService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/ultimas")
    public List<Postagem> listarUltimasPostagens() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        Usuario usuario = usuarioService.encontrarPorLogin(login);
        
        return postagemService.listarUltimas10PostagensDeTodosOsGruposDoUsuario(usuario);
    }
} 