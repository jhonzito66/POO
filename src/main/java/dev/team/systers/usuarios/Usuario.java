package dev.team.systers.usuarios;

import dev.team.systers.grupos.Membro;
import jakarta.persistence.*;

import java.util.List;
import java.util.TimeZone;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id", nullable = false)
    private Long id;
    @Column(name = "usuario_login", nullable = false)
    private String login;
    @Column(name = "usuario_senha", nullable = false)
    private String senha;
    @Column(name = "usuario_nome", nullable = false)
    private String nome;
    @Column(name = "usuario_email")
    private String email;
    @Column(name = "usuario_telefone")
    private String telefone;
    @Column(name = "usuario_fusoHorario")
    private TimeZone fusoHorario;
    @Column(name = "usuario_autorizacao")
    private String autorizacao; // nível de acesso: usuário padrão e usuário moderador
    @Column(name = "usuario_status_conta")
    private String statusConta;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Membro> membros;

    // ligação com denúncia aqui
}
