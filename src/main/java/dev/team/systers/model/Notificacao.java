package dev.team.systers.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
public class Notificacao {
    /**
     * Identificador único da notificação
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificacao_id", nullable = false)
    private Long id;

    /**
     * Conteúdo da notificação
     */
    @Column(name = "notificacao_conteudo", nullable = false)
    private String conteudo;

    /**
     * Data e hora de envio da notificação
     */
    @Column(name = "notificacao_data_hora", nullable = false)
    private LocalDateTime dataEnvio;

    /**
     * Estado da notificação: lido ou não (padrão)
     */
    @Column(name = "notificacao_status_lida", nullable = false)
    private boolean statusLida;

    /**
     * Usuário remetente associado à notificação
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_remetente_fk", foreignKey = @ForeignKey(name = "usuario_id_remetente_fk"), nullable = false)
    private Usuario usuarioNotificacaoRemetente;

    /**
     * Usuário destinatario associado à notificação
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_destinatario_fk", foreignKey = @ForeignKey(name = "usuario_id_destinatario_fk"), nullable = false)
    private Usuario usuarioNotificacaoDestinatario;

    public Notificacao() {
    }

    /**
     * Construtor sem parâmetros.
     * public Notificacao() {}
     * <p>
     * /**
     * Construtor completo.
     *
     * @param id
     * @param conteudo
     * @param dataEnvio
     * @param statusLida
     * @param usuarioNotificacaoRemetente
     * @param usuarioNotificacaoDestinatario
     */
    public Notificacao(Long id, String conteudo, LocalDateTime dataEnvio, boolean statusLida, Usuario usuarioNotificacaoRemetente, Usuario usuarioNotificacaoDestinatario) {
        this.id = id;
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
        this.statusLida = statusLida;
        this.usuarioNotificacaoRemetente = usuarioNotificacaoRemetente;
        this.usuarioNotificacaoDestinatario = usuarioNotificacaoDestinatario;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public boolean isStatusLida() {
        return statusLida;
    }

    public void setStatusLida(boolean statusLida) {
        this.statusLida = statusLida;
    }

    public Usuario getUsuarioNotificacaoRemetente() {
        return usuarioNotificacaoRemetente;
    }

    public void setUsuarioNotificacaoRemetente(Usuario usuarioNotificacaoRemetente) {
        this.usuarioNotificacaoRemetente = usuarioNotificacaoRemetente;
    }

    public Usuario getUsuarioNotificacaoDestinatario() {
        return usuarioNotificacaoDestinatario;
    }

    public void setUsuarioNotificacaoDestinatario(Usuario usuarioNotificacaoDestinatario) {
        this.usuarioNotificacaoDestinatario = usuarioNotificacaoDestinatario;
    }
}
