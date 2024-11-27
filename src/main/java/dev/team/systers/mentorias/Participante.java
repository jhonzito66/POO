package dev.team.systers.mentorias;

import dev.team.systers.usuarios.Usuario;
import jakarta.persistence.*;

/**
 * Representa um participante em uma mentoria.
 */
@Entity
@Table(name = "participante")
public class Participante {

    /**
     * Identificador único do participante.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participante_id", nullable = false)
    private Long id;

    /**
     * Nome do participante.
     * Este campo é obrigatório.
     */
    @Column(name = "participante_nome", nullable = false, length = 100)
    private String nome;

    /**
     * Tipo de participante na mentoria (MENTOR ou MENTORADO).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "participante_tipo", nullable = false)
    private TipoParticipante tipo;

    /**
     * Usuário associado ao participante.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_fk", foreignKey = @ForeignKey(name = "usuario_participante_fk"), nullable = false)
    private Usuario usuario;

    /**
     * Mentoria associada ao participante.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentoria_fk", foreignKey = @ForeignKey(name = "mentoria_participante_fk"), nullable = false)
    private Mentoria mentoria;

    /**
     * Construtor padrão.
     */
    public Participante() {}

    /**
     * Construtor completo.
     *
     * @param nome nome do participante
     * @param tipo tipo do participante (MENTOR ou MENTORADO)
     * @param usuario usuário associado ao participante
     * @param mentoria mentoria associada ao participante
     */
    public Participante(String nome, Usuario usuario, TipoParticipante tipo, Mentoria mentoria) {
        this.nome = nome;
        this.tipo = tipo;
        this.usuario = usuario;
        this.mentoria = mentoria;
    }

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

    public TipoParticipante getTipo() {
        return tipo;
    }

    public void setTipo(TipoParticipante tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Mentoria getMentoria() {
        return mentoria;
    }

    public void setMentoria(Mentoria mentoria) {
        this.mentoria = mentoria;
    }

    /**
     * Enum para representar os tipos de participantes.
     * Pode ser MENTOR ou MENTORADO.
     */
    public enum TipoParticipante {
        MENTOR,
        MENTORADO
    }
}
