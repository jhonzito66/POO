package dev.team.systers.mentorias;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mentoria")
public class Mentoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentoria_id", nullable = false)
    private Long id; // Identificador único da mentoria

    @Column(name = "mentoria_nome")
    private String nome; // Nome da mentoria

    @Column(name = "mentoria_data_hora_inicio")
    private LocalDateTime dataHoraInicio;

    @Column(name = "mentoria_data_hora_fim")
    private LocalDateTime dataHoraFim;

    @Column(name = "mentoria_status")
    private String status; // Status da mentoria

    // Relacionamento com o Mentee, chave estrangeira
    @ManyToOne
    @JoinColumn(name = "participante_mentee_fk", referencedColumnName = "id", nullable = false)
    private Mentee mentee; // O participante mentee

    // Relacionamento com o Mentor, chave estrangeira
    @ManyToOne
    @JoinColumn(name = "participante_mentor_fk", referencedColumnName = "id", nullable = false)
    private Mentor mentor; // O participante que é o mentor

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Mentee getMentee() {
        return mentee;
    }

    public void setMentee(Mentee mentee) {
        this.mentee = mentee;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }
}
