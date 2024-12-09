package dev.team.systers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import dev.team.systers.service.UsuarioService;

/**
 * Controlador responsável pelo gerenciamento da rota raiz da aplicação.
 * Direciona os usuários para as páginas apropriadas com base em seu estado de autenticação.
 */
@Controller
public class RootController {
    
    /**
     * Serviço que gerencia operações relacionadas a usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Construtor que inicializa o controlador com as dependências necessárias.
     * @param usuarioService Serviço de usuário injetado pelo Spring
     */
    @Autowired
    public RootController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Gerencia o acesso à rota raiz (/) da aplicação.
     * Redireciona usuários não autenticados para a página de login
     * e usuários autenticados para o feed.
     * 
     * @return String com o redirecionamento apropriado
     */
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

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }
}