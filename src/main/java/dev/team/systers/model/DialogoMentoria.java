package dev.team.systers.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Representa uma mensagem de diálogo em uma sessão de mentoria.
 * Esta classe gerencia a comunicação entre participantes durante uma mentoria,
 * mantendo o histórico de interações e mensagens trocadas.
 */
@Entity
@Table(name = "dialogo_mentoria")
public class DialogoMentoria {

    /**
     * Identificador único do diálogo.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dialogo_mentoria_id", nullable = false)
    private Long id;

    /**
     * Conteúdo da mensagem.
     * Texto da comunicação entre os participantes.
     */
    @Column(name = "dialogo_mentoria_mensagem")
    private String mensagem;

    /**
     * Mentoria associada ao diálogo.
     * Referência à sessão onde a mensagem foi enviada.
     */
    @ManyToOne
    @JoinColumn(name = "mentoria_fk", referencedColumnName = "mentoria_id", nullable = false)
    private Mentoria mentoria;

    /**
     * Participante que enviou a mensagem.
     * Referência ao autor da mensagem (mentor ou mentorado).
     */
    @ManyToOne
    @JoinColumn(name = "participante_fk", referencedColumnName = "participante_id", nullable = false)
    private Participante participante;

    /**
     * Data e hora do envio da mensagem.
     * Registrado automaticamente no momento do envio.
     */
    @Column(name = "dialogo_mentoria_data_hora")
    private LocalDateTime dataHora;

    /**
     * Construtor padrão.
     * Necessário para JPA.
     */
    public DialogoMentoria() {}

    /**
     * Construtor completo para criação de um diálogo.
     * @param id Identificador único
     * @param mensagem Conteúdo da mensagem
     * @param mentoria Sessão de mentoria
     * @param participante Autor da mensagem
     * @param dataHora Momento do envio
     */
    public DialogoMentoria(Long id, String mensagem, Mentoria mentoria, Participante participante, LocalDateTime dataHora) {
        this.id = id;
        this.mensagem = mensagem;
        this.mentoria = mentoria;
        this.participante = participante;
        this.dataHora = dataHora;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public Mentoria getMentoria() { return mentoria; }
    public void setMentoria(Mentoria mentoria) { this.mentoria = mentoria; }
    public Participante getParticipante() { return participante; }
    public void setParticipante(Participante participante) { this.participante = participante; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}
