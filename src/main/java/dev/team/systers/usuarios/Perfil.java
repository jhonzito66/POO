package dev.team.systers.usuarios;

import jakarta.persistence.*;

@Entity
@Table(name = "perfil")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfil_id", nullable = false)
    private Long id;

    @Column(name = "perfil_nome", nullable = false)
    private String perfil_nome;
    @Column(name = "perfil_bio")
    private String perfil_bio;
    @Column(name = "perfil_foto")
    private String perfil_foto;

    @OneToOne
    @JoinColumn(name = "usuario_id_perfil_fk", foreignKey = @ForeignKey(name = "usuario_id_perfil_fk"), nullable = false, unique = true)
    private Usuario usuario_perfil;

    public Perfil() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerfil_nome() {
        return perfil_nome;
    }

    public void setPerfil_nome(String perfil_nome) {
        this.perfil_nome = perfil_nome;
    }

    public String getPerfil_bio() {
        return perfil_bio;
    }

    public void setPerfil_bio(String perfil_bio) {
        this.perfil_bio = perfil_bio;
    }

    public String getPerfil_foto() {
        return perfil_foto;
    }

    public void setPerfil_foto(String perfil_foto) {
        this.perfil_foto = perfil_foto;
    }

    public Usuario getUsuario_perfil() {
        return usuario_perfil;
    }

    public void setUsuario_perfil(Usuario usuario_perfil) {
        this.usuario_perfil = usuario_perfil;
    }
}
