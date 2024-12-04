package dev.team.systers.controller;

import dev.team.systers.model.Mentoria;
import dev.team.systers.service.MentoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mentorias")
public class MentoriaController {

    private final MentoriaService mentoriaService;

    public MentoriaController(MentoriaService mentoriaService) {
        this.mentoriaService = mentoriaService;
    }

    /**
     * Lista todas as mentorias disponíveis.
     *
     * @return Lista de mentorias.
     */
    @GetMapping
    public ResponseEntity<List<Mentoria>> listarTodasMentorias() {
        return ResponseEntity.ok(mentoriaService.listarTodas());
    }

    /**
     * Busca mentorias por status.
     *
     * @param status Status da mentoria (ex: "ativa", "concluída").
     * @return Lista de mentorias com o status especificado.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Mentoria>> listarMentoriasPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(mentoriaService.listarPorStatus(status));
    }

    /**
     * Busca mentorias pelo nome (parcial ou completo, ignorando maiúsculas/minúsculas).
     *
     * @param nome Nome ou parte do nome da mentoria.
     * @return Lista de mentorias correspondentes.
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Mentoria>> listarMentoriasPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(mentoriaService.listarPorNome(nome));
    }

    /**
     * Busca mentorias que começam após uma data/hora específica.
     *
     * @param dataHoraInicio Data/hora de início (no formato ISO-8601).
     * @return Lista de mentorias.
     */
    @GetMapping("/inicio-apartir/{dataHoraInicio}")
    public ResponseEntity<List<Mentoria>> listarMentoriasPorInicio(@PathVariable String dataHoraInicio) {
        LocalDateTime inicio = LocalDateTime.parse(dataHoraInicio);
        return ResponseEntity.ok(mentoriaService.listarPorDataInicio(inicio));
    }

    /**
     * Cria uma nova mentoria.
     *
     * @param mentoria Dados da mentoria a ser criada.
     * @return Mentoria criada.
     */
    @PostMapping
    public ResponseEntity<Mentoria> criarMentoria(@RequestBody Mentoria mentoria) {
        try {
            Mentoria novaMentoria = mentoriaService.salvarMentoria(mentoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaMentoria);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
