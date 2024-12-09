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

/**
 * Controlador REST para operações relacionadas a postagens.
 * Fornece endpoints para acesso e gerenciamento de postagens nos grupos.
 */
@RestController
@RequestMapping("/api/postagens")
public class PostagemController {

    /**
     * Serviço que gerencia operações relacionadas a postagens.
     */
    private final PostagemService postagemService;

    /**
     * Serviço que gerencia operações relacionadas a usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Construtor que inicializa o controlador com as dependências necessárias.
     * @param postagemService Serviço de postagens injetado pelo Spring
     * @param usuarioService Serviço de usuário injetado pelo Spring
     */
    @Autowired
    public PostagemController(PostagemService postagemService, UsuarioService usuarioService) {
        this.postagemService = postagemService;
        this.usuarioService = usuarioService;
    }

    /**
     * Lista as últimas postagens dos grupos do usuário autenticado.
     * Retorna as 10 postagens mais recentes de todos os grupos
     * dos quais o usuário é membro.
     * 
     * @return Lista das últimas postagens
     */
    @GetMapping("/ultimas")
    public List<Postagem> listarUltimasPostagens() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        Usuario usuario = usuarioService.encontrarPorLogin(login);
        
        return postagemService.listarUltimas10PostagensDeTodosOsGruposDoUsuario(usuario);
    }
} 