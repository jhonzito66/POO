package dev.team.systers.suporte;

import dev.team.systers.usuarios.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Representa uma denúncia.
 * Contém informações sobre a denúncia, como a descrição, categoria, o autor e o usuário reportado.
 */
@Entity
@Table(name = "denuncia")
public class Denuncia {

    /**
     * Identificador único da denúncia (chave primária).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "denuncia_id", nullable = false)
    private Long id;

    /**
     * Descrição detalhada da denúncia.
     */
    @Column(name = "denuncia_descricao", nullable = false, length = 500)
    private String descricao;

    /**
     * Categoria da denúncia (ex: Comportamento inadequado, Assédio, etc.).
     */
    @Column(name = "denuncia_categoria", nullable = false, length = 100)
    private String categoria;

    /**
     * Status da denúncia (ex: Pendente, Em análise, Resolvida).
     */
    @Column(name = "denuncia_status", nullable = false, length = 50)
    private String status;

    /**
     * Data e hora da denúncia.
     */
    @Column(name = "denuncia_data_hora", nullable = false)
    private LocalDateTime dataHora;

    /**
     * Usuário autor da denúncia.
     * Relacionamento de muitos para um com a entidade Usuario.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_autor_fk", foreignKey = @ForeignKey(name = "usuario_autor_fk"), nullable = false)
    private Usuario usuarioAutor;

    /**
     * Usuário reportado na denúncia.
     * Relacionamento de muitos para um com a entidade Usuario.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_reportado_fk", foreignKey = @ForeignKey(name = "usuario_reportado_fk"), nullable = false)
    private Usuario usuarioReportado;

    // Construtores

    public Denuncia() {}

    public Denuncia(String descricao, String categoria, String status, LocalDateTime dataHora, Usuario usuarioAutor, Usuario usuarioReportado) {
        this.descricao = descricao;
        this.categoria = categoria;
        this.status = status;
        this.dataHora = dataHora;
        this.usuarioAutor = usuarioAutor;
        this.usuarioReportado = usuarioReportado;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Usuario getUsuarioAutor() {
        return usuarioAutor;
    }

    public void setUsuarioAutor(Usuario usuarioAutor) {
        this.usuarioAutor = usuarioAutor;
    }

    public Usuario getUsuarioReportado() {
        return usuarioReportado;
    }

    public void setUsuarioReportado(Usuario usuarioReportado) {
        this.usuarioReportado = usuarioReportado;
    }

    // Método toString para facilitar a visualização

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
