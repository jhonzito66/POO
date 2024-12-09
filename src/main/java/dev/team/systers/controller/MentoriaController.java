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

/**
 * Controlador responsável pelo gerenciamento de mentorias.
 * Gerencia operações de criação, visualização, finalização e avaliação
 * de sessões de mentoria entre mentores e mentorados.
 */
@Controller
public class MentoriaController {

    /**
     * Serviço que gerencia operações relacionadas a mentorias.
     */
    private final MentoriaService mentoriaService;

    /**
     * Serviço que gerencia operações relacionadas a usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Serviço que gerencia operações relacionadas a participantes.
     */
    private final ParticipanteService participanteService;

    /**
     * Construtor que inicializa o controlador com as dependências necessárias.
     * @param mentoriaService Serviço de mentoria injetado pelo Spring
     * @param usuarioService Serviço de usuário injetado pelo Spring
     * @param participanteService Serviço de participante injetado pelo Spring
     */
    @Autowired
    public MentoriaController(MentoriaService mentoriaService, UsuarioService usuarioService, ParticipanteService participanteService) {
        this.mentoriaService = mentoriaService;
        this.usuarioService = usuarioService;
        this.participanteService = participanteService;
    }

    /**
     * Exibe a página de mentorias para o usuário autenticado.
     * Lista todas as mentorias em que o usuário participa,
     * seja como mentor ou mentorado.
     *
     * @param model Modelo para passar dados à view
     * @return Nome da view de mentorias
     * @throws UsuarioException se o usuário não estiver autenticado ou não for encontrado
     * @throws MentoriaException se houver erro ao recuperar as mentorias
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

        List<Mentoria> mentorias = mentoriaService.listarMentoriasPorUsuario(usuario);
        if (mentorias == null) {
            throw new MentoriaException("Erro ao recuperar mentorias.");
        }

        model.addAttribute("mentorias", mentorias);
        model.addAttribute("mentoriaService", mentoriaService);

        return "mentorias";
    }

    /**
     * Cria uma nova sessão de mentoria.
     * Apenas usuários com status de mentor podem criar mentorias.
     *
     * @param nome Nome/título da mentoria
     * @param dataHoraInicio Data e hora de início da mentoria
     * @param dataHoraFim Data e hora de término da mentoria (opcional)
     * @param loginMentorado Login do usuário que será mentorado
     * @param model Modelo para passar dados à view
     * @return Redirecionamento para a lista de mentorias ou mensagem de erro
     */
    @PostMapping("/mentorias/criar-mentoria")
    public String criarMentoria(@RequestParam String nome,
                               @RequestParam LocalDateTime dataHoraInicio,
                               @RequestParam(required = false) LocalDateTime dataHoraFim,
                               @RequestParam String loginMentorado,
                               Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario mentor = usuarioService.encontrarPorLogin(auth.getName());
            
            if (mentor.getTipoMentor() == null || !mentor.getTipoMentor()) {
                throw new IllegalStateException("Usuário não é mentor");
            }
            
            Usuario mentorado = usuarioService.encontrarPorLogin(loginMentorado);
            if (mentorado == null) {
                throw new IllegalArgumentException("Usuário mentorado não encontrado");
            }
            
            Mentoria mentoria = new Mentoria();
            mentoria.setNome(nome);
            mentoria.setDataHoraInicio(dataHoraInicio);
            mentoria.setDataHoraFim(dataHoraFim);
            
            mentoria = mentoriaService.oferecerMentoria(mentoria, mentor);
            
            participanteService.criarParticipanteMentor(mentor, mentoria);
            participanteService.criarParticipanteMentorado(mentorado, mentoria);
            
            return "redirect:/mentorias";
            
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "redirect:/mentorias?erro=" + e.getMessage();
        }
    }

    /**
     * Finaliza uma sessão de mentoria.
     * Apenas o mentor responsável pode finalizar a mentoria.
     *
     * @param id ID da mentoria a ser finalizada
     * @return Redirecionamento para a lista de mentorias ou mensagem de erro
     */
    @GetMapping("/mentorias/finalizar/{id}")
    public String finalizarMentoria(@PathVariable Long id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario mentor = usuarioService.encontrarPorLogin(auth.getName());
            
            if (mentor.getTipoMentor() == null || !mentor.getTipoMentor()) {
                throw new IllegalStateException("Apenas mentores podem finalizar mentorias");
            }
            
            mentoriaService.finalizarMentoria(id);
            return "redirect:/mentorias";
            
        } catch (Exception e) {
            return "redirect:/mentorias?erro=" + e.getMessage();
        }
    }

    /**
     * Registra uma avaliação para uma mentoria.
     * Permite que participantes avaliem a qualidade da mentoria.
     *
     * @param mentoriaId ID da mentoria a ser avaliada
     * @param avaliacao Nota da avaliação (0-5)
     * @return Redirecionamento para a lista de mentorias ou mensagem de erro
     */
    @PostMapping("/mentorias/avaliar")
    public String avaliarMentoria(@RequestParam Long mentoriaId,
                                 @RequestParam int avaliacao) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuario = usuarioService.encontrarPorLogin(auth.getName());
            
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
