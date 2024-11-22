package dev.team.systers.grupos;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Representa um comentário feito por um membro em uma postagem.
 */
@Entity
@Table(name = "comentario")
public class Comentario {
    /**
     * Identificador único do comentário.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comentario_id", nullable = false)
    private Long id;

    /**
     * Conteúdo do comentário.
     * Obrigatório.
     */
    @Column(name = "comentario_conteudo", nullable = false, length = 1000)
    private String conteudo;

    /**
     * Data e hora da criação do comentário.
     * Obrigatório.
     */
    @Column(name = "comentario_data_hora", nullable = false)
    private LocalDateTime dataHora;

    /**
     * Postagem à qual o comentário pertence.
     * Relacionamento obrigatório.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postagem_fk", foreignKey = @ForeignKey(name = "postagem_comentario_fk"), nullable = false)
    private Postagem postagem;

    /**
     * Membro que fez o comentário.
     * Relacionamento obrigatório.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membro_autor_comentario_fk", foreignKey = @ForeignKey(name = "membro_comentario_fk"), nullable = false)
    private Membro autor;

    /**
     * Construtor padrão.
     */
    public Comentario() {}

    /**
     * Construtor completo.
     *
     * @param conteudo conteúdo do comentário
     * @param dataHora data e hora do comentário
     * @param postagem postagem relacionada
     * @param autor autor do comentário
     */
    public Comentario(String conteudo, LocalDateTime dataHora, Postagem postagem, Membro autor) {
        this.conteudo = conteudo;
        this.dataHora = dataHora;
        this.postagem = postagem;
        this.autor = autor;
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

    public Postagem getPostagem() {
        return postagem;
    }

    public void setPostagem(Postagem postagem) {
        this.postagem = postagem;
    }

    public Membro getAutor() {
        return autor;
    }

    public void setAutor(Membro autor) {
        this.autor = autor;
    }

}
