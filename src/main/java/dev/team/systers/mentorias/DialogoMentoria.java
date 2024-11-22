package dev.team.systers.mentorias;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dialogo_mentoria")
public class DialogoMentoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dialogo_mentoria_id", nullable = false)
    private Long id; // Identificador único do diálogo de mentoria

    @Column(name = "dialogo_mentoria_mensagem")
    private String mensagem; // Mensagem entre Mentor e Mentee

    @ManyToOne
    @JoinColumn(name = "mentoria_fk", referencedColumnName = "mentoria_id", nullable = false)
    private Mentoria mentoria; // Relacionamento com a mentoria que o diálogo pertence

    @ManyToOne
    @JoinColumn(name = "participante_mentor_fk", referencedColumnName = "id", nullable = false)
    private Mentor mentor; // Relacionamento com o Mentor

    @ManyToOne
    @JoinColumn(name = "participante_mentee_fk", referencedColumnName = "id", nullable = false)
    private Mentee mentee; // Relacionamento com o Mentee

    @Column(name = "dialogo_mentoria_data_hora")
    private LocalDateTime dataHora; // Data e hora em que a mensagem foi enviada

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

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public Mentee getMentee() {
        return mentee;
    }

    public void setMentee(Mentee mentee) {
        this.mentee = mentee;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
