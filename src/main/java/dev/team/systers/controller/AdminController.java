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

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UsuarioService usuarioService;

    @Autowired
    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

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