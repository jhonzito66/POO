package dev.team.systers.model;

import jakarta.persistence.*;

@Entity
@Table(name = "perfil")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfil_id", nullable = false)
    private Long id;

    @Column(name = "perfil_nome", nullable = false)
    private String perfilNome;
    @Column(name = "perfil_bio")
    private String perfilBio;
    @Column(name = "perfil_foto")
    private String perfilFoto;

    @OneToOne
    @JoinColumn(name = "usuario_id_perfil_fk", foreignKey = @ForeignKey(name = "usuario_id_perfil_fk"), nullable = false, unique = true)
    private Usuario usuarioPerfil;

    public Perfil() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerfilNome() {
        return perfilNome;
    }

    public void setPerfilNome(String perfilNome) {
        this.perfilNome = perfilNome;
    }

    public String getPerfilBio() {
        return perfilBio;
    }

    public void setPerfilBio(String perfilBio) {
        this.perfilBio = perfilBio;
    }

    public String getPerfilFoto() {
        return perfilFoto;
    }

    public void setPerfilFoto(String perfilFoto) {
        this.perfilFoto = perfilFoto;
    }

    public Usuario getUsuarioPerfil() {
        return usuarioPerfil;
    }

    public void setUsuarioPerfil(Usuario usuarioPerfil) {
        this.usuarioPerfil = usuarioPerfil;
    }
}
