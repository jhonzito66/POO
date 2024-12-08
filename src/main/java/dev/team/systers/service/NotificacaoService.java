package dev.team.systers.service;

import dev.team.systers.model.Notificacao;
import dev.team.systers.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacaoService {
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    public List<Notificacao> buscarNotificacoesDoUsuario(Long usuarioId) {
        return notificacaoRepository.findNotificacaosByUsuarioNotificacaoDestinatario_Id(usuarioId);
    }

    public Notificacao enviarNotificacao(Notificacao notificacao) {
        if (notificacao == null || notificacao.getUsuarioNotificacaoDestinatario() == null) {
            throw new IllegalArgumentException("Notificação ou destinatário não pode ser nulo.");
        }

        notificacao.setDataEnvio(LocalDateTime.now());
        notificacao.setStatusLida(false);

        return notificacaoRepository.save(notificacao);
    }

}
