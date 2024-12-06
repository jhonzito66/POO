package dev.team.systers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.team.systers.model.Postagem;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.PostagemService;

@RestController
@RequestMapping("/api/postagens")
public class PostagemController {

    private final PostagemService postagemService;

    @Autowired
    public PostagemController(PostagemService postagemService) {
        this.postagemService = postagemService;
    }

    @GetMapping("/ultimas")
    public List<Postagem> listarUltimasPostagens(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }
        return postagemService.listarUltimas10PostagensDeTodosOsGruposDoUsuario(usuario);
    }
} 