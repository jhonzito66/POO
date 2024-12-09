package dev.team.systers.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Representa um grupo no sistema.
 * Esta classe gerencia comunidades de usuários que compartilham interesses em comum,
 * permitindo a interação por postagens e discussões.
 */
@Entity
@Table(name = "grupo")
public class Grupo {
    /**
     * Identificador único do grupo.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grupo_id", nullable = false)
    private Long id;

    /**
     * Nome do grupo.
     * Identifica o grupo para os usuários.
     */
    @Column(name = "grupo_nome")
    private String nome;

    /**
     * Descrição do grupo.
     * Explica o propósito e as regras do grupo.
     */
    @Column(name = "grupo_descricao")
    private String descricao;

    /**
     * Status de atividade do grupo.
     * Indica se o grupo está ativo (true) ou inativo (false).
     */
    @Column(name = "grupo_status_ativo")
    private Boolean statusAtivo;

    /**
     * Lista de membros do grupo.
     * Gerencia todos os participantes e suas funções no grupo.
     */
    @JsonManagedReference(value = "grupo-membro")
    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Membro> membros;

    /**
     * Lista de postagens do grupo.
     * Contém todas as discussões e conteúdos compartilhados no grupo.
     */
    @JsonManagedReference(value = "grupo-postagem")
    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL)
    private List<Postagem> postagens;

    /**
     * Construtor padrão.
     * Necessário para JPA.
     */
    public Grupo() {}

    /**
     * Construtor completo para criação de um grupo.
     * @param id Identificador único
     * @param nome Nome do grupo
     * @param descricao Descrição e propósito do grupo
     * @param statusAtivo Estado de atividade do grupo
     * @param membros Lista inicial de membros
     */
    public Grupo(Long id, String nome, String descricao, Boolean statusAtivo, List<Membro> membros) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.statusAtivo = statusAtivo;
        this.membros = membros;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Boolean getStatusAtivo() { return statusAtivo; }
    public void setStatusAtivo(Boolean statusAtivo) { this.statusAtivo = statusAtivo; }
    public List<Membro> getMembros() { return membros; }
    public void setMembros(List<Membro> membros) { this.membros = membros; }
    public List<Postagem> getPostagens() { return postagens; }
    public void setPostagens(List<Postagem> postagens) { this.postagens = postagens; }
}
