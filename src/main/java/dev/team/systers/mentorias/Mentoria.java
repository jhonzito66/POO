package dev.team.systers.mentorias;

import jakarta.persistence.*;

/*
Exemplo: Mentee solicita uma sessão de mentoria com uma Mentora em determinada data e hora.
Mentoria: Representa uma sessão de mentoria com atributos como id, mentora, mentee, dataHora, status.
Agenda: Gerencia os horários disponíveis das Mentoras.
Banco de Dados: Tabelas para Mentorias, Agendamentos, Avaliacoes.
 */

@Entity
@Table(name = "mentoria")

public class Mentoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentoria_id", nullable = false)
    private Long id;
    @Column(name = "mentoria_nome")
    private String nome;
    //mentee
    @Column(name = "mentoria_dataHora")
    private String dataHora;
    @Column(name = "mentoria_status_ativo")
    private Boolean statusAtivo;

    /*
    @OneToMany(mappedBy = "mentoria", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Membro> membros;
     */

}

