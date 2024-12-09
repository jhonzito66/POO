package dev.team.systers.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Representa o perfil público de um usuário no sistema.
 * Esta classe gerencia as informações de apresentação do usuário,
 * incluindo nome de exibição, biografia e foto.
 */
@Entity
@Table(name = "perfil")
public class Perfil {
    /**
     * Identificador único do perfil.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfil_id", nullable = false)
    private Long id;

    /**
     * Nome de exibição do perfil.
     * Nome público que o usuário escolhe para se apresentar.
     * Campo obrigatório.
     */
    @Column(name = "perfil_nome", nullable = false)
    private String perfilNome;

    /**
     * Biografia do usuário.
     * Texto livre para o usuário se apresentar e descrever suas experiências.
     * Campo opcional.
     */
    @Column(name = "perfil_bio")
    private String perfilBio;

    /**
     * Foto do perfil.
     * Referência para a imagem de perfil do usuário.
     * Campo opcional.
     */
    @Column(name = "perfil_foto")
    private String perfilFoto;

    /**
     * Usuário associado ao perfil.
     * Cada perfil pertence a um único usuário e vice-versa.
     */
    @JsonBackReference(value = "usuario-perfil")
    @OneToOne
    @JoinColumn(name = "usuario_id_perfil_fk", foreignKey = @ForeignKey(name = "usuario_id_perfil_fk"), nullable = false, unique = true)
    private Usuario usuarioPerfil;

    /**
     * Construtor padrão.
     * Necessário para JPA.
     */
    public Perfil() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPerfilNome() { return perfilNome; }
    public void setPerfilNome(String perfilNome) { this.perfilNome = perfilNome; }
    public String getPerfilBio() { return perfilBio; }
    public void setPerfilBio(String perfilBio) { this.perfilBio = perfilBio; }
    public String getPerfilFoto() { return perfilFoto; }
    public void setPerfilFoto(String perfilFoto) { this.perfilFoto = perfilFoto; }
    public Usuario getUsuarioPerfil() { return usuarioPerfil; }
    public void setUsuarioPerfil(Usuario usuarioPerfil) { this.usuarioPerfil = usuarioPerfil; }
}
