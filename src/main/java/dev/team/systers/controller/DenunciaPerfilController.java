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

/**
 * Controlador REST para operações relacionadas a denúncias de perfis.
 * Gerencia a listagem e visualização de denúncias, com funcionalidades
 * específicas para usuários comuns e administradores.
 */
@RestController
@RequestMapping("/api/denuncias")
public class DenunciaPerfilController {
    
    /**
     * Serviço que gerencia operações relacionadas a denúncias.
     */
    private final DenunciaService denunciaService;

    /**
     * Serviço que gerencia operações relacionadas a usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Construtor que inicializa o controlador com as dependências necessárias.
     * @param denunciaService Serviço de denúncia injetado pelo Spring
     * @param usuarioService Serviço de usuário injetado pelo Spring
     */
    @Autowired
    public DenunciaPerfilController(DenunciaService denunciaService, UsuarioService usuarioService) {
        this.denunciaService = denunciaService;
        this.usuarioService = usuarioService;
    }

    /**
     * Lista as denúncias relevantes para o usuário autenticado.
     * Para administradores, retorna todas as denúncias pendentes.
     * Para usuários comuns, retorna apenas suas próprias denúncias.
     * 
     * @return ResponseEntity contendo a lista de denúncias apropriada
     */
    @GetMapping("/minhas")
    public ResponseEntity<List<Denuncia>> listarMinhasDenuncias() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        Usuario usuario = usuarioService.encontrarPorLogin(login);
        
        List<Denuncia> denuncias;
        if (usuario.getAutorizacao() == Usuario.Autorizacao.ADMINISTRADOR) {
            denuncias = denunciaService.listarPendentes();
        } else {
            denuncias = denunciaService.listarPorUsuarioAutor(usuario.getId());
        }
        
        return ResponseEntity.ok(denuncias);
    }

    /**
     * Lista todas as denúncias do sistema.
     * Endpoint restrito a administradores.
     * 
     * @return ResponseEntity contendo todas as denúncias ou FORBIDDEN se não for administrador
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