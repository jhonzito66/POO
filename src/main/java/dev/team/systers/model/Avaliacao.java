package dev.team.systers.model;

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
 * Representa uma avaliação de mentoria no sistema.
 * Esta classe gerencia o feedback dos participantes sobre as sessões de mentoria,
 * permitindo avaliar a qualidade e efetividade das mentorias realizadas.
 */
@Entity
@Table(name = "avaliacao")
public class Avaliacao {
    /**
     * Identificador único da avaliação.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avaliacao_id")
    private Long id;

    /**
     * Nota atribuída à mentoria.
     * Escala de 0 a 5, onde:
     * 0 = Insatisfatório
     * 5 = Excelente
     */
    @Column(name = "avaliacao_mentoria")
    private int avaliacaoMentoria;

    /**
     * Mentoria que está sendo avaliada.
     * Referência à sessão que recebeu o feedback.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentoria_avaliada_fk", foreignKey = @ForeignKey(name = "mentoria_avaliada_fk"), nullable = false)
    private Mentoria mentoriaAvaliada;

    /**
     * Usuário que realizou a avaliação.
     * Geralmente é o mentorado que avalia a sessão.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_mentee_avaliador_fk", foreignKey = @ForeignKey(name = "usuario_mentee_avaliador_fk"), nullable = false)
    private Usuario participanteAvaliador;

    /**
     * Construtor padrão.
     * Necessário para JPA.
     */
    public Avaliacao() {}

    /**
     * Construtor completo para criação de uma avaliação.
     * @param id Identificador único
     * @param avaliacaoMentoria Nota atribuída (0-5)
     * @param mentoriaAvaliada Sessão avaliada
     * @param participanteAvaliador Usuário que avaliou
     */
    public Avaliacao(Long id, int avaliacaoMentoria, Mentoria mentoriaAvaliada, Usuario participanteAvaliador) {
        this.id = id;
        this.avaliacaoMentoria = avaliacaoMentoria;
        this.mentoriaAvaliada = mentoriaAvaliada;
        this.participanteAvaliador = participanteAvaliador;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getAvaliacaoMentoria() { return avaliacaoMentoria; }
    public void setAvaliacaoMentoria(int avaliacaoMentoria) { this.avaliacaoMentoria = avaliacaoMentoria; }
    public Mentoria getMentoriaAvaliada() { return mentoriaAvaliada; }
    public void setMentoriaAvaliada(Mentoria mentoriaAvaliada) { this.mentoriaAvaliada = mentoriaAvaliada; }
    public Usuario getParticipanteAvaliador() { return participanteAvaliador; }
    public void setParticipanteAvaliador(Usuario participanteAvaliador) { this.participanteAvaliador = participanteAvaliador; }
}
