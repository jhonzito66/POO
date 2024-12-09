package dev.team.systers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Notificacao;

/**
 * Repositório para operações de persistência de Notificação.
 * Fornece métodos para acessar e manipular dados de notificações do sistema.
 */
@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    
    /**
     * Busca todas as notificações recebidas por um usuário.
     * @param usuarioId ID do usuário destinatário
     * @return Lista de notificações do usuário
     */
    List<Notificacao> findNotificacaosByUsuarioNotificacaoDestinatario_Id(Long usuarioId);
}
