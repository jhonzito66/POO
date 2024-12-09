package dev.team.systers.controller;

import dev.team.systers.model.DialogoMentoria;
import dev.team.systers.model.Mentoria;
import dev.team.systers.model.Participante;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.DialogoMentoriaService;
import dev.team.systers.service.MentoriaService;
import dev.team.systers.service.ParticipanteService;
import dev.team.systers.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/mentorias/dialogo")
public class DialogoMentoriaController {

    private final DialogoMentoriaService dialogoMentoriaService;
    private final MentoriaService mentoriaService;
    private final ParticipanteService participanteService;
    private final UsuarioService usuarioService;

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

    @GetMapping("/{mentoriaId}/mensagens")
    @ResponseBody
    public List<DialogoMentoria> buscarMensagens(@PathVariable Long mentoriaId) {
        return dialogoMentoriaService.listarPorMentoria(mentoriaId);
    }
} 