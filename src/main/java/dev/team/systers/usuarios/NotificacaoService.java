package dev.team.systers.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void enviarNotificacao(Notificacao notificacao) {
        // TODO: implementar
    }
}
