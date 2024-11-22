package dev.team.systers.grupos;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa uma postagem em um grupo.
 */
@Entity
@Table(name = "postagem")
public class Postagem {
    /**
     * Identificador único da postagem.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postagem_id", nullable = false)
    private Long id;

    /**
     * Conteúdo da postagem.
     */
    @Column(name = "postagem_conteudo", nullable = false, length = 2000)
    private String conteudo;

    /**
     * Data e hora de criação da postagem.
     */
    @Column(name = "postagem_data_hora", nullable = false)
    private LocalDateTime dataHora;

    /**
     * Autor da postagem.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membro_autor_postagem_fk", foreignKey = @ForeignKey(name = "membro_postagem_fk"), nullable = false)
    private Membro autor;

    /**
     * Grupo ao qual a postagem pertence.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_destinatario_fk", foreignKey = @ForeignKey(name = "grupo_postagem_fk"), nullable = false)
    private Grupo grupo;

    /**
     * Comentários associados à postagem.
     */
    @OneToMany(mappedBy = "postagem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios;

    // Construtor padrão
    public Postagem() {}

    // Construtor completo
    public Postagem(String conteudo, LocalDateTime dataHora, Membro autor, Grupo grupo) {
        this.conteudo = conteudo;
        this.dataHora = dataHora;
        this.autor = autor;
        this.grupo = grupo;
    }

    // Getters e Setters

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

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Membro getAutor() {
        return autor;
    }

    public void setAutor(Membro autor) {
        this.autor = autor;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

}
