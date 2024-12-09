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

/**
 * Controlador REST para operações relacionadas a usuários.
 * Fornece endpoints para gerenciamento de usuários,
 * com foco em funcionalidades administrativas.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioApiController {
    
    /**
     * Serviço que gerencia operações relacionadas a usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Construtor que inicializa o controlador com as dependências necessárias.
     * @param usuarioService Serviço de usuário injetado pelo Spring
     */
    @Autowired
    public UsuarioApiController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Lista todos os usuários que possuem denúncias.
     * Endpoint restrito a administradores.
     * 
     * @return Lista de usuários denunciados
     * @throws AccessDeniedException se o usuário não for administrador
     */
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

    /**
     * Atualiza o status de um usuário específico.
     * Endpoint restrito a administradores.
     * 
     * @param id ID do usuário a ser atualizado
     * @param status Novo status a ser definido
     * @return ResponseEntity com o usuário atualizado ou erro apropriado
     */
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

    /**
     * Classe interna para deserialização do status na atualização.
     * Usada para receber o novo status do usuário via JSON.
     */
    private static class StatusUpdateRequest {
        private String status;
        
        /**
         * Obtém o status solicitado.
         * @return String representando o status
         */
        public String getStatus() {
            return status;
        }
        
        /**
         * Define o status a partir do JSON.
         * @param status String representando o novo status
         */
        @JsonProperty("status")
        public void setStatus(String status) {
            this.status = status;
        }
    }
} 