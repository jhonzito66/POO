package dev.team.systers.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.model.Notificacao;
import dev.team.systers.repository.NotificacaoRepository;

/**
 * Serviço responsável pelo gerenciamento de notificações no sistema.
 * Fornece funcionalidades para envio e consulta de notificações entre usuários.
 */
@Service
public class NotificacaoService {
    
    /**
     * Repositório para acesso aos dados de notificações.
     */
    private final NotificacaoRepository notificacaoRepository;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     * @param notificacaoRepository Repositório de notificações injetado pelo Spring
     */
    @Autowired
    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    /**
     * Busca todas as notificações de um usuário específico.
     * 
     * @param usuarioId ID do usuário destinatário das notificações
     * @return Lista de notificações do usuário
     */
    public List<Notificacao> buscarNotificacoesDoUsuario(Long usuarioId) {
        return notificacaoRepository.findNotificacaosByUsuarioNotificacaoDestinatario_Id(usuarioId);
    }

    /**
     * Envia uma nova notificação para um usuário.
     * Define automaticamente a data de envio e marca como não lida.
     * 
     * @param notificacao Objeto contendo os dados da notificação a ser enviada
     * @return Notificação salva no sistema
     * @throws IllegalArgumentException se a notificação ou destinatário for nulo
     */
    public Notificacao enviarNotificacao(Notificacao notificacao) {
        if (notificacao == null || notificacao.getUsuarioNotificacaoDestinatario() == null) {
            throw new IllegalArgumentException("Notificação ou destinatário não pode ser nulo.");
        }

        notificacao.setDataEnvio(LocalDateTime.now());
        notificacao.setStatusLida(false);

        return notificacaoRepository.save(notificacao);
    }
}
