package dev.team.systers.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dialogo_mentoria")
public class DialogoMentoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dialogo_mentoria_id", nullable = false)
    private Long id;

    @Column(name = "dialogo_mentoria_mensagem")
    private String mensagem;

    @ManyToOne
    @JoinColumn(name = "mentoria_fk", referencedColumnName = "mentoria_id", nullable = false)
    private Mentoria mentoria;

    @ManyToOne
    @JoinColumn(name = "participante_fk", referencedColumnName = "participante_id", nullable = false)
    private Participante participante;  // Referência ao Participante

    @Column(name = "dialogo_mentoria_data_hora")
    private LocalDateTime dataHora;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Mentoria getMentoria() {
        return mentoria;
    }

    public void setMentoria(Mentoria mentoria) {
        this.mentoria = mentoria;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
