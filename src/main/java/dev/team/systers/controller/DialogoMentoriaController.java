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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.team.systers.model.DialogoMentoria;
import dev.team.systers.model.Mentoria;
import dev.team.systers.model.Participante;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.DialogoMentoriaService;
import dev.team.systers.service.MentoriaService;
import dev.team.systers.service.ParticipanteService;
import dev.team.systers.service.UsuarioService;

/**
 * Controlador responsável pelo gerenciamento de diálogos em mentorias.
 * Gerencia a comunicação entre mentor e mentorado durante as sessões,
 * permitindo troca de mensagens e acompanhamento do histórico de diálogos.
 */
@Controller
@RequestMapping("/mentorias/dialogo")
public class DialogoMentoriaController {

    /**
     * Serviço que gerencia operações relacionadas a diálogos.
     */
    private final DialogoMentoriaService dialogoMentoriaService;

    /**
     * Serviço que gerencia operações relacionadas a mentorias.
     */
    private final MentoriaService mentoriaService;

    /**
     * Serviço que gerencia operações relacionadas a participantes.
     */
    private final ParticipanteService participanteService;

    /**
     * Serviço que gerencia operações relacionadas a usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Construtor que inicializa o controlador com as dependências necessárias.
     * @param dialogoMentoriaService Serviço de diálogo injetado pelo Spring
     * @param mentoriaService Serviço de mentoria injetado pelo Spring
     * @param participanteService Serviço de participante injetado pelo Spring
     * @param usuarioService Serviço de usuário injetado pelo Spring
     */
    @Autowired
    public DialogoMentoriaController(DialogoMentoriaService dialogoMentoriaService,
                                   MentoriaService mentoriaService,
                                   ParticipanteService participanteService,
                                   UsuarioService usuarioService) {
        this.dialogoMentoriaService = dialogoMentoriaService;
        this.mentoriaService = mentoriaService;
        this.participanteService = participanteService;
        this.usuarioService = usuarioService;
    }

    /**
     * Exibe a página de diálogo de uma mentoria específica.
     * Carrega o histórico de mensagens e informações dos participantes.
     * 
     * @param mentoriaId ID da mentoria
     * @param model Modelo para passar dados à view
     * @return Nome da view de diálogo
     */
    @GetMapping("/{mentoriaId}")
    public String mostrarDialogo(@PathVariable Long mentoriaId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.buscarPorLogin(auth.getName());
        
        Mentoria mentoria = mentoriaService.buscarPorId(mentoriaId);
        List<DialogoMentoria> dialogos = dialogoMentoriaService.listarPorMentoria(mentoriaId);
        
        model.addAttribute("mentoria", mentoria);
        model.addAttribute("dialogos", dialogos);
        model.addAttribute("usuario", usuario);
        
        return "dialogo-mentoria";
    }

    /**
     * Processa o envio de uma nova mensagem no diálogo.
     * Registra a mensagem com data e hora atual e o participante que enviou.
     * 
     * @param mentoriaId ID da mentoria
     * @param mensagem Conteúdo da mensagem
     * @return Redirecionamento para a página de diálogo
     */
    @PostMapping("/enviar")
    public String enviarMensagem(@RequestParam Long mentoriaId,
                                @RequestParam String mensagem) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.buscarPorLogin(auth.getName());
        
        Mentoria mentoria = mentoriaService.buscarPorId(mentoriaId);
        Participante participante = participanteService.buscarParticipantePorUsuarioEMentoria(usuario, mentoria);
        
        DialogoMentoria dialogo = new DialogoMentoria();
        dialogo.setMensagem(mensagem);
        dialogo.setMentoria(mentoria);
        dialogo.setParticipante(participante);
        dialogo.setDataHora(LocalDateTime.now());
        
        dialogoMentoriaService.salvarDialogo(dialogo);
        
        return "redirect:/mentorias/dialogo/" + mentoriaId;
    }

    /**
     * Retorna a lista de mensagens de uma mentoria específica.
     * Endpoint utilizado para atualização assíncrona do diálogo.
     * 
     * @param mentoriaId ID da mentoria
     * @return Lista de mensagens do diálogo
     */
    @GetMapping("/{mentoriaId}/mensagens")
    @ResponseBody
    public List<DialogoMentoria> buscarMensagens(@PathVariable Long mentoriaId) {
        return dialogoMentoriaService.listarPorMentoria(mentoriaId);
    }
} 