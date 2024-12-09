package dev.team.systers.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Representa um membro de um grupo no sistema.
 * Esta classe gerencia a participação de usuários em grupos,
 * definindo seus papéis, permissões e status dentro de cada grupo.
 */
@Entity
@Table(name = "membro")
public class Membro {
    /**
     * Identificador único do membro.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membro_id", nullable = false)
    private Long id;

    /**
     * Tag identificadora do membro no grupo.
     * Corresponde ao login do usuário associado.
     * Campo obrigatório e único dentro do grupo.
     */
    @Column(name = "membro_tag", nullable = false)
    private String tag;

    /**
     * Nome de exibição do membro no grupo.
     * Corresponde ao nome do usuário associado.
     * Campo obrigatório para identificação visual.
     */
    @Column(name = "membro_nome", nullable = false)
    private String nome;

    /**
     * Nível de autorização do membro no grupo.
     * Define as permissões e capacidades do membro:
     * - DONO: Criador do grupo, com controle total
     * - MODERADOR: Designado pelo dono para auxiliar na administração
     * - PADRAO: Membro comum com permissões básicas
     * @see Autorizacao
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "membro_autorizacao", nullable = false)
    private Autorizacao autorizacao;

    /**
     * Status atual do membro no grupo.
     * Controla o acesso do membro às funcionalidades do grupo.
     * @see StatusAcesso
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "membro_status_acesso", nullable = false)
    private StatusAcesso statusAcesso;

    /**
     * Usuário associado ao membro.
     * Referência ao usuário do sistema que participa do grupo.
     */
    @JsonBackReference(value = "usuario-membro")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_membro_fk", foreignKey = @ForeignKey(name = "usuario_id_fk"), nullable = false)
    private Usuario usuario;

    /**
     * Grupo ao qual o membro pertence.
     * Referência ao grupo onde o usuário participa.
     */
    @JsonBackReference(value = "grupo-membro")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id_membro_fk", foreignKey = @ForeignKey(name = "grupo_id_fk"), nullable = false)
    private Grupo grupo;

    /**
     * Construtor padrão.
     * Necessário para JPA.
     */
    public Membro() {}

    /**
     * Construtor completo para criação de um membro.
     * @param id Identificador único
     * @param tag Tag identificadora no grupo
     * @param nome Nome de exibição
     * @param autorizacao Nível de autorização
     * @param statusAcesso Status de acesso
     * @param usuario Usuário associado
     * @param grupo Grupo ao qual pertence
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
     * Níveis de autorização disponíveis para membros no grupo.
     */
    public enum Autorizacao {
        /** Membro com permissões básicas */
        PADRAO,
        /** Membro com permissões de moderação */
        MODERADOR,
        /** Criador do grupo com controle total */
        DONO
    }

    /**
     * Status possíveis para um membro no grupo.
     */
    public enum StatusAcesso {
        /** Acesso normal às funcionalidades do grupo */
        NORMAL,
        /** Acesso temporariamente restrito */
        SUSPENSO,
        /** Acesso permanentemente bloqueado */
        BANIDO
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Autorizacao getAutorizacao() { return autorizacao; }
    public void setAutorizacao(Autorizacao autorizacao) { this.autorizacao = autorizacao; }
    public StatusAcesso getStatusAcesso() { return statusAcesso; }
    public void setStatusAcesso(StatusAcesso statusAcesso) { this.statusAcesso = statusAcesso; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
}
