package dev.team.systers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.team.systers.model.Usuario;
import dev.team.systers.service.UsuarioService;

/**
 * Controlador responsável pelas operações administrativas da aplicação.
 * Gerencia funcionalidades restritas a usuários com privilégios de administrador,
 * como moderação de usuários e gerenciamento de status de contas.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    /**
     * Serviço que gerencia operações relacionadas a usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Construtor que inicializa o controlador com as dependências necessárias.
     * @param usuarioService Serviço de usuário injetado pelo Spring
     */
    @Autowired
    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Atualiza o status da conta de um usuário específico.
     * Permite que administradores alterem o estado de uma conta
     * (ex: ATIVO, SUSPENSO, BANIDO).
     * 
     * @param id ID do usuário a ser atualizado
     * @param status Novo status da conta
     * @param redirectAttributes Atributos para mensagens de feedback
     * @return Redirecionamento para a página de feed
     */
    @PostMapping("/usuarios/{id}/status")
    public String atualizarStatusUsuario(@PathVariable Long id, 
                                        @RequestParam String status, 
                                        RedirectAttributes redirectAttributes) {
        try {
            Usuario.StatusConta novoStatus = Usuario.StatusConta.valueOf(status);
            usuarioService.atualizarStatusUsuario(id, novoStatus);
            redirectAttributes.addFlashAttribute("mensagem", "Status do usuário atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar status: " + e.getMessage());
        }
        return "redirect:/feed";
    }
} 