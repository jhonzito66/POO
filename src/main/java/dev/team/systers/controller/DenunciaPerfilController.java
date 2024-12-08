package dev.team.systers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.team.systers.model.Denuncia;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.DenunciaService;
import dev.team.systers.service.UsuarioService;

@RestController
@RequestMapping("/api/denuncias-perfil")
public class DenunciaPerfilController {
    
    private final DenunciaService denunciaService;
    private final UsuarioService usuarioService;

    @Autowired
    public DenunciaPerfilController(DenunciaService denunciaService, UsuarioService usuarioService) {
        this.denunciaService = denunciaService;
        this.usuarioService = usuarioService;
    }

    /**
     * Lista as denúncias feitas pelo usuário logado
     */
    @GetMapping("/minhas")
    public ResponseEntity<List<Denuncia>> listarMinhasDenuncias() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        Usuario usuario = usuarioService.encontrarPorLogin(login);
        
        List<Denuncia> denuncias = denunciaService.listarPorUsuarioAutor(usuario.getId());
        return ResponseEntity.ok(denuncias);
    }

    /**
     * Lista todas as denúncias (apenas para administradores)
     */
    @GetMapping("/todas")
    public ResponseEntity<List<Denuncia>> listarTodasDenuncias() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        Usuario admin = usuarioService.encontrarPorLogin(login);
        
        if (admin.getAutorizacao() != Usuario.Autorizacao.ADMINISTRADOR) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(denunciaService.listarTodas());
    }
} 