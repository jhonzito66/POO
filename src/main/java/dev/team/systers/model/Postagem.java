package dev.team.systers.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * Representa uma postagem no sistema.
 * Esta classe gerencia o conteúdo compartilhado pelos membros em grupos,
 * incluindo o texto da postagem, autor, grupo e comentários associados.
 */
@Entity
public class Postagem {
    /**
     * Identificador único da postagem.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Conteúdo textual da postagem.
     * Limitado a 1000 caracteres.
     */
    @Column(nullable = false, length = 1000)
    private String conteudo;

    /**
     * Membro que criou a postagem.
     * Representa o autor do conteúdo.
     */
    @JsonManagedReference(value = "postagem-membro")
    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Membro autor;

    /**
     * Grupo onde a postagem foi feita.
     * Cada postagem pertence a um único grupo.
     */
    @JsonBackReference(value = "grupo-postagem")
    @ManyToOne
    @JoinColumn(name = "grupo_id", nullable = false)
    private Grupo grupo;

    /**
     * Data e hora de criação da postagem.
     * Definido automaticamente no momento da criação.
     */
    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    /**
     * Lista de comentários feitos na postagem.
     * Gerenciados em cascata com a postagem.
     */
    @OneToMany(mappedBy = "postagem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comentario> comentarios = new ArrayList<>();

    /**
     * Construtor padrão.
     * Necessário para JPA.
     */
    public Postagem() {}

    /**
     * Construtor completo para criação de uma postagem.
     * @param id Identificador único
     * @param conteudo Texto da postagem
     * @param autor Membro que criou a postagem
     * @param grupo Grupo onde a postagem foi feita
     * @param dataCriacao Momento da criação
     */
    public Postagem(Long id, String conteudo, Membro autor, Grupo grupo, LocalDateTime dataCriacao) {
        this.id = id;
        this.conteudo = conteudo;
        this.autor = autor;
        this.grupo = grupo;
        this.dataCriacao = dataCriacao;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public Membro getAutor() { return autor; }
    public void setAutor(Membro autor) { this.autor = autor; }
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public List<Comentario> getComentarios() { return comentarios; }
    public void setComentarios(List<Comentario> comentarios) { this.comentarios = comentarios; }
}
