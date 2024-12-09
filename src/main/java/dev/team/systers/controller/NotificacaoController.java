package dev.team.systers.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.team.systers.model.Notificacao;
import dev.team.systers.repository.NotificacaoRepository;
import dev.team.systers.service.NotificacaoService;

/**
 * Controlador REST para operações relacionadas a notificações.
 * Gerencia o envio, listagem e atualização de notificações do sistema,
 * permitindo a comunicação assíncrona com os usuários.
 */
@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    /**
     * Serviço que gerencia operações relacionadas a notificações.
     */
    private final NotificacaoService notificacaoService;

    /**
     * Repositório para acesso direto aos dados de notificações.
     */
    private final NotificacaoRepository notificacaoRepository;

    /**
     * Construtor que inicializa o controlador com as dependências necessárias.
     * @param notificacaoService Serviço de notificação injetado pelo Spring
     * @param notificacaoRepository Repositório de notificação injetado pelo Spring
     */
    public NotificacaoController(NotificacaoService notificacaoService, NotificacaoRepository notificacaoRepository) {
        this.notificacaoService = notificacaoService;
        this.notificacaoRepository = notificacaoRepository;
    }

    /**
     * Lista todas as notificações de um usuário específico.
     * Retorna as notificações ordenadas por data de envio.
     *
     * @param usuarioId ID do usuário destinatário
     * @return ResponseEntity contendo a lista de notificações do usuário
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacao>> listarNotificacoesPorUsuario(@PathVariable Long usuarioId) {
        List<Notificacao> notificacoes = notificacaoService.buscarNotificacoesDoUsuario(usuarioId);
        return ResponseEntity.ok(notificacoes);
    }

    /**
     * Cria e envia uma nova notificação.
     * Define automaticamente a data de envio e marca como não lida.
     *
     * @param notificacao Dados da notificação a ser criada
     * @return ResponseEntity contendo a notificação criada
     */
    @PostMapping
    public ResponseEntity<Notificacao> enviarNotificacao(@RequestBody Notificacao notificacao) {
        notificacao.setDataEnvio(LocalDateTime.now());
        notificacao.setStatusLida(false);
        Notificacao novaNotificacao = notificacaoRepository.save(notificacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaNotificacao);
    }

    /**
     * Marca uma notificação específica como lida.
     * Atualiza o status da notificação no banco de dados.
     *
     * @param id ID da notificação a ser marcada como lida
     * @return ResponseEntity contendo a notificação atualizada ou NOT_FOUND se não existir
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
