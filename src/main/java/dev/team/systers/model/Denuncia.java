package dev.team.systers.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
 * Representa uma denúncia no sistema.
 * Esta classe gerencia as denúncias feitas por usuários contra outros usuários,
 * permitindo o acompanhamento e moderação do comportamento na plataforma.
 */
@Entity
@Table(name = "denuncia")
public class Denuncia {

    /**
     * Identificador único da denúncia.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "denuncia_id", nullable = false)
    private Long id;

    /**
     * Descrição detalhada da denúncia.
     * Contém os motivos e detalhes específicos da denúncia.
     * Limitado a 500 caracteres.
     */
    @Column(name = "denuncia_descricao", nullable = false, length = 500)
    private String descricao;

    /**
     * Categoria da denúncia.
     * Classifica o tipo de violação reportada (ex: Assédio, Spam, Conteúdo Inadequado).
     * Limitado a 100 caracteres.
     */
    @Column(name = "denuncia_categoria", nullable = false, length = 100)
    private String categoria;

    /**
     * Status atual da denúncia.
     * Indica o estado de processamento da denúncia pela moderação.
     * @see StatusDenuncia
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "denuncia_status", nullable = false)
    private StatusDenuncia status = StatusDenuncia.PENDENTE;

    /**
     * Data e hora do registro da denúncia.
     * Registrado automaticamente no momento da criação.
     */
    @Column(name = "denuncia_data_hora", nullable = false)
    private LocalDateTime dataHora;

    /**
     * Usuário que criou a denúncia.
     * Referência ao usuário que reportou o comportamento inadequado.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_autor_fk", foreignKey = @ForeignKey(name = "usuario_autor_fk"), nullable = false)
    @JsonIgnoreProperties({"denunciasCriadas", "denunciasRecebidas"})
    private Usuario usuarioAutor;

    /**
     * Usuário que está sendo denunciado.
     * Referência ao usuário cujo comportamento está sendo reportado.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_reportado_fk", foreignKey = @ForeignKey(name = "usuario_reportado_fk"), nullable = false)
    @JsonIgnoreProperties({"denunciasCriadas", "denunciasRecebidas"})
    private Usuario usuarioReportado;

    /**
     * Construtor padrão.
     * Necessário para JPA.
     */
    public Denuncia() {}

    /**
     * Construtor completo para criação de uma denúncia.
     * @param id Identificador único
     * @param descricao Detalhes da denúncia
     * @param categoria Tipo de violação reportada
     * @param status Estado atual da denúncia
     * @param dataHora Momento do registro
     * @param usuarioAutor Usuário que fez a denúncia
     * @param usuarioReportado Usuário que está sendo denunciado
     */
    public Denuncia(Long id, String descricao, String categoria, StatusDenuncia status, LocalDateTime dataHora, Usuario usuarioAutor, Usuario usuarioReportado) {
        this.id = id;
        this.descricao = descricao;
        this.categoria = categoria;
        this.status = status;
        this.dataHora = dataHora;
        this.usuarioAutor = usuarioAutor;
        this.usuarioReportado = usuarioReportado;
    }

    /**
     * Status possíveis para uma denúncia.
     */
    public enum StatusDenuncia {
        /** Denúncia aguardando análise da moderação */
        PENDENTE,
        /** Denúncia já analisada e processada pela moderação */
        ATENDIDA
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public StatusDenuncia getStatus() { return status; }
    public void setStatus(StatusDenuncia status) { this.status = status; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public Usuario getUsuarioAutor() { return usuarioAutor; }
    public void setUsuarioAutor(Usuario usuarioAutor) { this.usuarioAutor = usuarioAutor; }
    public Usuario getUsuarioReportado() { return usuarioReportado; }
    public void setUsuarioReportado(Usuario usuarioReportado) { this.usuarioReportado = usuarioReportado; }

    @Override
    public String toString() {
        return "Denuncia{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", categoria='" + categoria + '\'' +
                ", status='" + status + '\'' +
                ", dataHora=" + dataHora +
                ", usuarioAutor=" + usuarioAutor +
                ", usuarioReportado=" + usuarioReportado +
                '}';
    }
}
