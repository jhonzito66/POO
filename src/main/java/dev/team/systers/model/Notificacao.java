package dev.team.systers.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Representa uma notificação no sistema.
 * Esta classe gerencia as mensagens de notificação entre usuários,
 * permitindo a comunicação assíncrona e o acompanhamento de status de leitura.
 */
@Entity
@Table(name = "notificacao")
public class Notificacao {
    /**
     * Identificador único da notificação.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificacao_id", nullable = false)
    private Long id;

    /**
     * Conteúdo da notificação.
     * Mensagem a ser entregue ao destinatário.
     */
    @Column(name = "notificacao_conteudo", nullable = false)
    private String conteudo;

    /**
     * Data e hora de envio da notificação.
     * Registrado automaticamente no momento do envio.
     */
    @Column(name = "notificacao_data_hora", nullable = false)
    private LocalDateTime dataEnvio;

    /**
     * Status de leitura da notificação.
     * Indica se o destinatário já visualizou a mensagem.
     * true = lida, false = não lida
     */
    @Column(name = "notificacao_status_lida", nullable = false)
    private boolean statusLida;

    /**
     * Usuário que enviou a notificação.
     * Referência ao remetente da mensagem.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_remetente_fk", foreignKey = @ForeignKey(name = "usuario_id_remetente_fk"), nullable = false)
    private Usuario usuarioNotificacaoRemetente;

    /**
     * Usuário que recebeu a notificação.
     * Referência ao destinatário da mensagem.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_destinatario_fk", foreignKey = @ForeignKey(name = "usuario_id_destinatario_fk"), nullable = false)
    private Usuario usuarioNotificacaoDestinatario;

    /**
     * Construtor padrão.
     * Necessário para JPA.
     */
    public Notificacao() {}

    /**
     * Construtor completo para criação de uma notificação.
     * @param id Identificador único
     * @param conteudo Mensagem da notificação
     * @param dataEnvio Momento do envio
     * @param statusLida Estado de leitura
     * @param usuarioNotificacaoRemetente Usuário que enviou
     * @param usuarioNotificacaoDestinatario Usuário que recebeu
     */
    public Notificacao(Long id, String conteudo, LocalDateTime dataEnvio, boolean statusLida, Usuario usuarioNotificacaoRemetente, Usuario usuarioNotificacaoDestinatario) {
        this.id = id;
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
        this.statusLida = statusLida;
        this.usuarioNotificacaoRemetente = usuarioNotificacaoRemetente;
        this.usuarioNotificacaoDestinatario = usuarioNotificacaoDestinatario;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }
    public boolean isStatusLida() { return statusLida; }
    public void setStatusLida(boolean statusLida) { this.statusLida = statusLida; }
    public Usuario getUsuarioNotificacaoRemetente() { return usuarioNotificacaoRemetente; }
    public void setUsuarioNotificacaoRemetente(Usuario usuarioNotificacaoRemetente) { this.usuarioNotificacaoRemetente = usuarioNotificacaoRemetente; }
    public Usuario getUsuarioNotificacaoDestinatario() { return usuarioNotificacaoDestinatario; }
    public void setUsuarioNotificacaoDestinatario(Usuario usuarioNotificacaoDestinatario) { this.usuarioNotificacaoDestinatario = usuarioNotificacaoDestinatario; }
}
