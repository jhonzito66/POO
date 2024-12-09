package dev.team.systers.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Representa um comentário em uma postagem no sistema.
 * Esta classe gerencia as interações dos usuários em postagens,
 * permitindo discussões e feedback sobre o conteúdo compartilhado.
 */
@Entity
public class Comentario {
    /**
     * Identificador único do comentário.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Conteúdo do comentário.
     * Texto limitado a 500 caracteres.
     */
    @Column(nullable = false, length = 500)
    private String conteudo;

    /**
     * Data e hora de criação do comentário.
     * Registrado automaticamente no momento da criação.
     */
    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    /**
     * Membro que criou o comentário.
     * Referência ao autor do comentário.
     */
    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Membro autor;

    /**
     * Postagem onde o comentário foi feito.
     * Referência à postagem que recebeu o comentário.
     */
    @ManyToOne
    @JoinColumn(name = "postagem_id")
    private Postagem postagem;

    /**
     * Construtor padrão.
     * Necessário para JPA.
     */
    public Comentario() {}

    /**
     * Construtor completo para criação de um comentário.
     * @param id Identificador único
     * @param conteudo Texto do comentário
     * @param dataCriacao Momento da criação
     * @param autor Membro que criou o comentário
     * @param postagem Postagem relacionada
     */
    public Comentario(Long id, String conteudo, LocalDateTime dataCriacao, Membro autor, Postagem postagem) {
        this.id = id;
        this.conteudo = conteudo;
        this.dataCriacao = dataCriacao;
        this.autor = autor;
        this.postagem = postagem;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public Membro getAutor() { return autor; }
    public void setAutor(Membro autor) { this.autor = autor; }
    public Postagem getPostagem() { return postagem; }
    public void setPostagem(Postagem postagem) { this.postagem = postagem; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime now) { this.dataCriacao = now; }
}
