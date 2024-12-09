package dev.team.systers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import dev.team.systers.model.Postagem;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.PostagemService;
import dev.team.systers.service.UsuarioService;

/**
 * Controlador responsável pelo gerenciamento do feed de postagens.
 * Exibe as postagens mais recentes dos grupos que o usuário participa
 * e informações adicionais para administradores.
 */
@Controller
public class FeedController {
    
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
    public FeedController(PostagemService postagemService, UsuarioService usuarioService) {
        this.postagemService = postagemService;
        this.usuarioService = usuarioService;
    }

    /**
     * Exibe o feed personalizado do usuário autenticado.
     * Carrega as 10 postagens mais recentes dos grupos do usuário
     * e, para administradores, exibe também a lista de usuários denunciados.
     * 
     * @param model Modelo para passar dados à view
     * @return Nome da view do feed
     */
    @GetMapping("/feed")
    public String exibirFeed(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        Usuario usuario = usuarioService.encontrarPorLogin(login);
        
        List<Postagem> postagens = postagemService.listarUltimas10PostagensDeTodosOsGruposDoUsuario(usuario);
        model.addAttribute("postagens", postagens);
        
        if (usuario.getAutorizacao() == Usuario.Autorizacao.ADMINISTRADOR) {
            List<Usuario> usuariosDenunciados = usuarioService.listarUsuariosDenunciados();
            model.addAttribute("usuariosDenunciados", usuariosDenunciados);
        }
        
        model.addAttribute("usuario", usuario);
        return "feed";
    }
}
