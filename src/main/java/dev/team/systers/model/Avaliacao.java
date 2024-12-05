package dev.team.systers.model;

import jakarta.persistence.*;

// Essa entidade não precisava existir no banco de dados para evitar um ciclo de relações.
// Mantém-se em razão do enunciado da atividade.
@Entity
@Table(name = "avaliacao")
public class Avaliacao {
    /**
     * Identificador único da avaliação.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avaliacao_id")
    private Long id;

    /**
     * Avaliação da mentoria. Um número inteiro de 0 a 5.
     */
    @Column(name = "avaliacao_mentoria")
    private int avaliacaoMentoria;

    /**
     * Mentoria associada à avaliação.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentoria_avaliada_fk", foreignKey = @ForeignKey(name = "mentoria_avaliada_fk"), nullable = false)
    private Mentoria mentoriaAvaliada;

    /**
     * Participante associada à avaliação.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_mentee_avaliador_fk", foreignKey = @ForeignKey(name = "usuario_mentee_avaliador_fk"), nullable = false)
    private Usuario participanteAvaliador;

    /**
     * Construtor vazio.
     */
    public Avaliacao() {}

    /**
     * Construtor completo.
     *
     * @param id
     * @param avaliacaoMentoria
     * @param mentoriaAvaliada
     * @param participanteAvaliador
     */
    public Avaliacao(Long id, int avaliacaoMentoria, Mentoria mentoriaAvaliada, Usuario participanteAvaliador) {
        this.id = id;
        this.avaliacaoMentoria = avaliacaoMentoria;
        this.mentoriaAvaliada = mentoriaAvaliada;
        this.participanteAvaliador = participanteAvaliador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAvaliacaoMentoria() {
        return avaliacaoMentoria;
    }

    public void setAvaliacaoMentoria(int avaliacaoMentoria) {
        this.avaliacaoMentoria = avaliacaoMentoria;
    }

    public Mentoria getMentoriaAvaliada() {
        return mentoriaAvaliada;
    }

    public void setMentoriaAvaliada(Mentoria mentoriaAvaliada) {
        this.mentoriaAvaliada = mentoriaAvaliada;
    }

    public Usuario getParticipanteAvaliador() {
        return participanteAvaliador;
    }

    public void setParticipanteAvaliador(Usuario participanteAvaliador) {
        this.participanteAvaliador = participanteAvaliador;
    }
}
