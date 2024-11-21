package dev.team.systers.model.grupos;

/*
Exemplo: Usuárias podem criar e participar de grupos de networking com interesses comuns.

Grupo: Representa um grupo com atributos id, nome, descricao, listaDeMembros.
Mensagem: Permite troca de mensagens dentro do grupo.
Banco de Dados: Tabelas para Grupos, Usuarios_Grupos, Mensagens.
 */

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
