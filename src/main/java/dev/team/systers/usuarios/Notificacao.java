package dev.team.systers.usuarios;

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
     * Usuário associado à notificação
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_membro_fk", foreignKey = @ForeignKey(name = "usuario_id_fk"), nullable = false)
    private Usuario usuarioNotificacao;

    /** Construtor sem parâmetros.
    public Notificacao() {}

    /**
     * Construtor completo.
     * @param id
     * @param conteudo
     * @param dataEnvio
     * @param statusLida
     * @param usuarioNotificacao
     */
    public Notificacao(Long id, String conteudo, LocalDateTime dataEnvio, boolean statusLida, Usuario usuarioNotificacao) {
        this.id = id;
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
        this.statusLida = statusLida;
        this.usuarioNotificacao = usuarioNotificacao;
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

    public Usuario getUsuarioNotificacao() {
        return usuarioNotificacao;
    }

    public void setUsuarioNotificacao(Usuario usuarioNotificacao) {
        this.usuarioNotificacao = usuarioNotificacao;
    }
}
