package dev.team.systers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

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
        Usuario admin = usuarioService.encontrarPorLogin(login);
        
        if (admin.getAutorizacao() != Usuario.Autorizacao.ADMINISTRADOR) {
            throw new AccessDeniedException("Apenas administradores podem ver usuários denunciados");
        }
        
        return usuarioService.listarUsuariosDenunciados();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Usuario> atualizarStatusUsuario(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest status) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String login = auth.getName();
            Usuario admin = usuarioService.encontrarPorLogin(login);
            
            if (admin.getAutorizacao() != Usuario.Autorizacao.ADMINISTRADOR) {
                throw new AccessDeniedException("Apenas administradores podem alterar status de usuários");
            }
            
            Usuario usuarioAtualizado = usuarioService.atualizarStatusUsuario(id, Usuario.StatusConta.valueOf(status.getStatus()));
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // Classe interna para receber o status
    private static class StatusUpdateRequest {
        private String status;
        
        public String getStatus() {
            return status;
        }
        
        @JsonProperty("status")
        public void setStatus(String status) {
            this.status = status;
        }
    }
} 