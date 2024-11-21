package dev.team.systers.grupos;

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
}
