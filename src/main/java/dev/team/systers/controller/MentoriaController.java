package dev.team.systers.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.team.systers.exception.MentoriaException;
import dev.team.systers.exception.UsuarioException;
import dev.team.systers.model.Avaliacao;
import dev.team.systers.model.Mentoria;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.MentoriaService;
import dev.team.systers.service.ParticipanteService;
import dev.team.systers.service.UsuarioService;

@Controller
public class MentoriaController {

    private final MentoriaService mentoriaService;
    private final UsuarioService usuarioService;
    private final ParticipanteService participanteService;

    @Autowired
    public MentoriaController(MentoriaService mentoriaService, UsuarioService usuarioService, ParticipanteService participanteService) {
        this.mentoriaService = mentoriaService;
        this.usuarioService = usuarioService;
        this.participanteService = participanteService;
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
        Usuario usuario = usuarioService.encontrarPorLogin(username);
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
        model.addAttribute("mentoriaService", mentoriaService);

        return "mentorias";
    }

    @PostMapping("/mentorias/criar-mentoria")
    public String criarMentoria(@RequestParam String nome,
                               @RequestParam LocalDateTime dataHoraInicio,
                               @RequestParam(required = false) LocalDateTime dataHoraFim,
                               @RequestParam String loginMentorado,
                               Model model) {
        try {
            // Obter usuário mentor (logado)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario mentor = usuarioService.encontrarPorLogin(auth.getName());
            
            // Verificar se é mentor
            if (mentor.getTipoMentor() == null || !mentor.getTipoMentor()) {
                throw new IllegalStateException("Usuário não é mentor");
            }
            
            // Obter usuário mentorado
            Usuario mentorado = usuarioService.encontrarPorLogin(loginMentorado);
            if (mentorado == null) {
                throw new IllegalArgumentException("Usuário mentorado não encontrado");
            }
            
            // Criar nova mentoria
            Mentoria mentoria = new Mentoria();
            mentoria.setNome(nome);
            mentoria.setDataHoraInicio(dataHoraInicio);
            mentoria.setDataHoraFim(dataHoraFim);
            
            // Salvar mentoria
            mentoria = mentoriaService.oferecerMentoria(mentoria, mentor);
            
            // Criar participantes
            participanteService.criarParticipanteMentor(mentor, mentoria);
            participanteService.criarParticipanteMentorado(mentorado, mentoria);
            
            return "redirect:/mentorias";
            
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "redirect:/mentorias?erro=" + e.getMessage();
        }
    }

    @GetMapping("/mentorias/finalizar/{id}")
    public String finalizarMentoria(@PathVariable Long id) {
        try {
            // Obter usuário mentor (logado)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario mentor = usuarioService.encontrarPorLogin(auth.getName());
            
            // Verificar se é mentor
            if (mentor.getTipoMentor() == null || !mentor.getTipoMentor()) {
                throw new IllegalStateException("Apenas mentores podem finalizar mentorias");
            }
            
            mentoriaService.finalizarMentoria(id);
            return "redirect:/mentorias";
            
        } catch (Exception e) {
            return "redirect:/mentorias?erro=" + e.getMessage();
        }
    }

    @PostMapping("/mentorias/avaliar")
    public String avaliarMentoria(@RequestParam Long mentoriaId,
                                 @RequestParam int avaliacao) {
        try {
            // Obter usuário logado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuario = usuarioService.encontrarPorLogin(auth.getName());
            
            // Criar avaliação
            Avaliacao novaAvaliacao = new Avaliacao();
            novaAvaliacao.setAvaliacaoMentoria(avaliacao);
            novaAvaliacao.setParticipanteAvaliador(usuario);
            
            Mentoria mentoria = mentoriaService.buscarPorId(mentoriaId);
            novaAvaliacao.setMentoriaAvaliada(mentoria);
            
            mentoriaService.avaliarMentoria(novaAvaliacao);
            
            return "redirect:/mentorias";
            
        } catch (Exception e) {
            return "redirect:/mentorias?erro=" + e.getMessage();
        }
    }
}
