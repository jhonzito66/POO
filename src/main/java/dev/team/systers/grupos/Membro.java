package dev.team.systers.grupos;

import dev.team.systers.usuarios.Usuario;
import jakarta.persistence.*;

/**
 * Representa um membro de um grupo.
 */
@Entity
@Table(name = "membro")
public class Membro {
    /**
     * Identificador único do membro.
     * Obrigatório (não há membro sem ID).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membro_id", nullable = false)
    private Long id;

    /**
     * Tag do membro.
     * Deve ser igual ao login do usuário.
     * Obrigatório.
     */
    @Column(name = "membro_tag", nullable = false)
    private String tag;

    /**
     * Nome do membro.
     * Deve ser igual ao nome do usuário.
     * Obrigatório.
     */
    @Column(name = "membro_nome", nullable = false)
    private String nome;

    /**
     * Nível de acesso do membro no grupo (padrão, moderador ou dono).
     * Quem cria o grupo se torna dono.
     * O dono pode escolher moderadores.
     * Quem entra se torna membro padrão.
     * Obrigatório.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "membro_autorizacao", nullable = false)
    private Autorizacao autorizacao;

    /**
     * StatusConta de acesso do membro no grupo (normal, suspenso, banido).
     * Obrigatório.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "membro_status_acesso", nullable = false)
    private StatusAcesso statusAcesso;

    /**
     * Usuário associado ao membro do grupo.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_membro_fk", foreignKey = @ForeignKey(name = "usuario_id_fk"), nullable = false)
    private Usuario usuario;

    /**
     * Grupo associado ao membro.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id_membro_fk", foreignKey = @ForeignKey(name = "grupo_id_fk"), nullable = false)
    private Grupo grupo;

    /**
     * Construtor sem parâmetros.
     */
    public Membro() {}

    /**
     * Construtor completo.
     * @param id
     * @param tag
     * @param nome
     * @param autorizacao
     * @param statusAcesso
     * @param usuario
     * @param grupo
     */
    public Membro(Long id, String tag, String nome, Autorizacao autorizacao, StatusAcesso statusAcesso, Usuario usuario, Grupo grupo) {
        this.id = id;
        this.tag = tag;
        this.nome = nome;
        this.autorizacao = autorizacao;
        this.statusAcesso = statusAcesso;
        this.usuario = usuario;
        this.grupo = grupo;
    }

    /**
     * Enum para representar os níveis de autorização dos membros.
     * Pode ser <i>PADRAO</i>, <i>MODERADOR</i> OU <i>DONO</i>.
     */
    public enum Autorizacao {
        PADRAO,
        MODERADOR,
        DONO
    }

    /**
     * Enum para representar os status de acesso dos membros.
     * Pode sr <i>NORMAL</i>, <i>SUSPENSO</i> ou <i>BANIDO</i>.
     */
    public enum StatusAcesso {
        NORMAL,
        SUSPENSO,
        BANIDO
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Autorizacao getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(Autorizacao autorizacao) {
        this.autorizacao = autorizacao;
    }

    public StatusAcesso getStatusAcesso() {
        return statusAcesso;
    }

    public void setStatusAcesso(StatusAcesso statusAcesso) {
        this.statusAcesso = statusAcesso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
