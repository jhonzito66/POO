package dev.team.systers.model.Usuario;

import dev.team.systers.model.grupos.Membro;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id", nullable = false)
    private Long id;
    @Column(name = "usuario_login")
    private String login;
    @Column(name = "usuario_senha")
    private String senha;
    @Column(name = "usuario_nome")
    private String nome;
    @Column(name = "usuario_email")
    private String email;
    @Column(name = "usuario_telefone")
    private String telefone;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Membro> membros;

}
