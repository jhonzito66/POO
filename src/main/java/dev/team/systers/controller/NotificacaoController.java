package dev.team.systers.controller;

import dev.team.systers.model.Notificacao;
import dev.team.systers.repository.NotificacaoRepository;
import dev.team.systers.service.NotificacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;
    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoController(NotificacaoService notificacaoService, NotificacaoRepository notificacaoRepository) {
        this.notificacaoService = notificacaoService;
        this.notificacaoRepository = notificacaoRepository;
    }

    /**
     * Lista todas as notificações de um usuário específico.
     *
     * @param usuarioId ID do destinatário.
     * @return Lista de notificações do usuário.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacao>> listarNotificacoesPorUsuario(@PathVariable Long usuarioId) {
        List<Notificacao> notificacoes = notificacaoService.buscarNotificacoesDoUsuario(usuarioId);
        return ResponseEntity.ok(notificacoes);
    }

    /**
     * Envia uma nova notificação.
     *
     * @param notificacao Dados da notificação.
     * @return Notificação criada.
     */
    @PostMapping
    public ResponseEntity<Notificacao> enviarNotificacao(@RequestBody Notificacao notificacao) {
        notificacao.setDataEnvio(LocalDateTime.now()); // Define data/hora de envio
        notificacao.setStatusLida(false); // Define como não lida por padrão
        Notificacao novaNotificacao = notificacaoRepository.save(notificacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaNotificacao);
    }

    /**
     * Marca uma notificação como lida.
     *
     * @param id ID da notificação.
     * @return Notificação atualizada ou erro 404.
     */
    @PutMapping("/{id}/lida")
    public ResponseEntity<Notificacao> marcarComoLida(@PathVariable Long id) {
        return notificacaoRepository.findById(id).map(notificacao -> {
            notificacao.setStatusLida(true);
            Notificacao notificacaoAtualizada = notificacaoRepository.save(notificacao);
            return ResponseEntity.ok(notificacaoAtualizada);
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
