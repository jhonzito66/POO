package dev.team.systers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import dev.team.systers.model.Perfil;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.UsuarioService;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registrar")
    public String exibirFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrar";
    }

    @PostMapping("/registrar_usuario")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
        try {
            usuarioService.registrar(usuario.getLogin().toLowerCase(), 
                                   usuario.getSenha(), 
                                   usuario.getEmail().toLowerCase(),
                                   usuario.getNome(), 
                                   usuario.getTelefone(), 
                                   usuario.getFusoHorario());
            return "login";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", e.getMessage());
            return "registrar";
        }
    }

    @GetMapping("/login")
    public String exibirFormularioLogin(Model model) {
        return "login";
    }

}
