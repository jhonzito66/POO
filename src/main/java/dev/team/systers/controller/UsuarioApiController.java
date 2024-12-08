package dev.team.systers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.team.systers.model.Usuario;
import dev.team.systers.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioApiController {
    
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioApiController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/denunciados")
    public List<Usuario> listarUsuariosDenunciados() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        Usuario admin = usuarioService.findByLogin(login);
        
        if (admin.getAutorizacao() != Usuario.Autorizacao.ADMINISTRADOR) {
            throw new AccessDeniedException("Apenas administradores podem ver usuários denunciados");
        }
        
        return usuarioService.listarUsuariosDenunciados();
    }
} 