package dev.team.systers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import dev.team.systers.exception.MentoriaException;
import dev.team.systers.exception.UsuarioException;
import dev.team.systers.model.Mentoria;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.MentoriaService;
import dev.team.systers.service.UsuarioService;

@Controller
public class MentoriaController {

    private final MentoriaService mentoriaService;
    private final UsuarioService usuarioService;

    @Autowired
    public MentoriaController(MentoriaService mentoriaService, UsuarioService usuarioService) {
        this.mentoriaService = mentoriaService;
        this.usuarioService = usuarioService;
    }

    /**
     * Exibe a página de mentorias para o usuário autenticado.
     *
     * @param model Modelo para passar dados para a view.
     * @return Nome da view correspondente.
     */
    @GetMapping("/mentorias")
    public String exibirMentorias(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getName().equals("anonymousUser")) {
            throw new UsuarioException("Usuário não autenticado");
        }

        String username = auth.getName();
        Usuario usuario = usuarioService.findByLogin(username);
        if (usuario == null) {
            throw new UsuarioException("Usuário não encontrado");
        }
        model.addAttribute("usuario", usuario);

        // Obter mentorias do usuário
        List<Mentoria> mentorias = mentoriaService.listarMentoriasPorUsuario(usuario);
        if (mentorias == null) {
            throw new MentoriaException("Erro ao recuperar mentorias.");
        }

        model.addAttribute("mentorias", mentorias);

        return "mentorias";
    }
}
