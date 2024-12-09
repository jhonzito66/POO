package dev.team.systers.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Representa uma sessão de mentoria no sistema.
 * Esta classe gerencia as sessões de mentoria entre mentores e mentorados,
 * incluindo agendamento, participantes e avaliações.
 */
@Entity
@Table(name = "mentoria")
public class Mentoria {

    /**
     * Identificador único da mentoria.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentoria_id", nullable = false)
    private Long id;

    /**
     * Nome ou título da mentoria.
     * Descreve brevemente o tema ou objetivo da sessão.
     */
    @Column(name = "mentoria_nome")
    private String nome;

    /**
     * Data e hora de início da mentoria.
     * Momento agendado para o começo da sessão.
     */
    @Column(name = "mentoria_data_hora_inicio")
    private LocalDateTime dataHoraInicio;

    /**
     * Data e hora de término da mentoria.
     * Momento previsto para o encerramento da sessão.
     */
    @Column(name = "mentoria_data_hora_fim")
    private LocalDateTime dataHoraFim;

    /**
     * Status atual da mentoria.
     * Indica o estado da sessão (ex: Agendada, Em Andamento, Concluída).
     */
    @Column(name = "mentoria_status")
    private String status;

    /**
     * Lista de participantes da mentoria.
     * Inclui tanto mentores quanto mentorados.
     */
    @OneToMany(mappedBy = "mentoria")
    private List<Participante> participantes;

    /**
     * Avaliações recebidas pela mentoria.
     * Feedback dos participantes sobre a sessão.
     */
    @OneToMany(mappedBy = "mentoriaAvaliada")
    private List<Avaliacao> avaliacoesMentoria;

    /**
     * Construtor padrão.
     * Necessário para JPA.
     */
    public Mentoria() {}

    /**
     * Construtor completo para criação de uma mentoria.
     * @param id Identificador único
     * @param nome Título da mentoria
     * @param dataHoraInicio Momento de início
     * @param dataHoraFim Momento de término
     * @param status Estado atual
     * @param participantes Lista de participantes
     * @param avaliacoesMentoria Avaliações recebidas
     */
    public Mentoria(Long id, String nome, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, String status, List<Participante> participantes, List<Avaliacao> avaliacoesMentoria) {
        this.id = id;
        this.nome = nome;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.status = status;
        this.participantes = participantes;
        this.avaliacoesMentoria = avaliacoesMentoria;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public void setDataHoraInicio(LocalDateTime dataHoraInicio) { this.dataHoraInicio = dataHoraInicio; }
    public LocalDateTime getDataHoraFim() { return dataHoraFim; }
    public void setDataHoraFim(LocalDateTime dataHoraFim) { this.dataHoraFim = dataHoraFim; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<Participante> getParticipantes() { return participantes; }
    public void setParticipantes(List<Participante> participantes) { this.participantes = participantes; }
    public List<Avaliacao> getAvaliacoesMentoria() { return avaliacoesMentoria; }
    public void setAvaliacoesMentoria(List<Avaliacao> avaliacoesMentoria) { this.avaliacoesMentoria = avaliacoesMentoria; }
}
