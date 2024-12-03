package dev.team.systers.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "grupo")
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grupo_id", nullable = false)
    private Long id;
    @Column(name = "grupo_nome")
    private String nome;
    @Column(name = "grupo_descricao")
    private String descricao;
    @Column(name = "grupo_status_ativo")
    private Boolean statusAtivo;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Membro> membros;

    /**
     * Construtor sem parâmetros.
     */
    public Grupo() {}

    /**
     * Construtor completo.
     * @param id
     * @param nome
     * @param descricao
     * @param statusAtivo
     * @param membros
     */
    public Grupo(Long id, String nome, String descricao, Boolean statusAtivo, List<Membro> membros) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.statusAtivo = statusAtivo;
        this.membros = membros;
    }

    // Getters & Setters

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getStatusAtivo() {
        return statusAtivo;
    }

    public void setStatusAtivo(Boolean statusAtivo) {
        this.statusAtivo = statusAtivo;
    }

    public List<Membro> getMembros() {
        return membros;
    }

    public void setMembros(List<Membro> membros) {
        this.membros = membros;
    }
}
