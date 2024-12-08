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

    @GetMapping("/perfil/{usuarioId}")
    public String visualizarPerfil(@PathVariable Long usuarioId, Model model) {
        try {
            Perfil perfil = usuarioService.visualizarPerfil(usuarioId);
            model.addAttribute("perfil", perfil);
            return "perfil";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao visualizar perfil: " + e.getMessage());
            return "erro";
        }
    }

    @GetMapping("/perfil/editar/{usuarioId}")
    public String exibirFormularioEditarPerfil(@PathVariable Long usuarioId, Model model) {
        try {
            Usuario usuario = usuarioService.encontrarPorID(usuarioId);
            model.addAttribute("usuario", usuario);
            return "editarPerfil";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao carregar perfil: " + e.getMessage());
            return "erro";
        }
    }

    @PostMapping("/perfil/editar/{usuarioId}")
    public String editarPerfil(@PathVariable Long usuarioId, @ModelAttribute Usuario usuarioAtualizado, Model model) {
        try {
            usuarioService.editarPerfil(usuarioId, 
                                      usuarioAtualizado.getNome(), 
                                      usuarioAtualizado.getEmail(), 
                                      usuarioAtualizado.getTelefone());
            return "redirect:/perfil/" + usuarioId;
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao atualizar perfil: " + e.getMessage());
            return "editarPerfil";
        }
    }
}
