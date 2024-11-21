package dev.team.systers.model.grupos;

import dev.team.systers.model.Usuario.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "membro")
public class Membro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membro_id", nullable = false)
    private Long id;
    @Column(name = "membro_nome")
    private String nome; // obter o nome do usuário
    @Column(name = "membro_autorizacao")
    private String autorizacao; // nível de acesso do membro do grupo: padrão ou moderador
    @Column(name = "membro_status_acesso")
    private String statusAcesso; // normal, suspenso, banido

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id", foreignKey = @ForeignKey(name = "grupo_id_fkey"), nullable = false)
    private Grupo grupo;
}
