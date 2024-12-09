package dev.team.systers.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Representa um participante em uma mentoria no sistema.
 * Esta classe gerencia os usuários que participam de sessões de mentoria,
 * definindo seus papéis como mentor ou mentorado.
 */
@Entity
@Table(name = "participante")
public class Participante {

    /**
     * Identificador único do participante.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participante_id", nullable = false)
    private Long id;

    /**
     * Nome do participante na mentoria.
     * Identificação visual do participante durante a sessão.
     * Limitado a 100 caracteres.
     */
    @Column(name = "participante_nome", nullable = false, length = 100)
    private String nome;

    /**
     * Função do participante na mentoria.
     * Define se o participante atua como mentor ou mentorado.
     * @see TipoParticipante
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "participante_tipo", nullable = false)
    private TipoParticipante tipo;

    /**
     * Usuário associado ao participante.
     * Referência ao usuário do sistema que participa da mentoria.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_fk", foreignKey = @ForeignKey(name = "usuario_participante_fk"), nullable = false)
    private Usuario usuario;

    /**
     * Mentoria da qual o usuário participa.
     * Referência à sessão de mentoria específica.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentoria_fk", foreignKey = @ForeignKey(name = "mentoria_participante_fk"), nullable = false)
    private Mentoria mentoria;

    /**
     * Construtor padrão.
     * Necessário para JPA.
     */
    public Participante() {}

    /**
     * Construtor completo para criação de um participante.
     * @param nome Nome de exibição na mentoria
     * @param usuario Usuário do sistema
     * @param tipo Função na mentoria (mentor/mentorado)
     * @param mentoria Sessão de mentoria
     */
    public Participante(String nome, Usuario usuario, TipoParticipante tipo, Mentoria mentoria) {
        this.nome = nome;
        this.tipo = tipo;
        this.usuario = usuario;
        this.mentoria = mentoria;
    }

    /**
     * Tipos de participação possíveis em uma mentoria.
     */
    public enum TipoParticipante {
        /** Usuário que compartilha conhecimento e experiência */
        MENTOR,
        /** Usuário que busca aprendizado e orientação */
        MENTORADO
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public TipoParticipante getTipo() { return tipo; }
    public void setTipo(TipoParticipante tipo) { this.tipo = tipo; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Mentoria getMentoria() { return mentoria; }
    public void setMentoria(Mentoria mentoria) { this.mentoria = mentoria; }
}
