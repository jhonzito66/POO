package dev.team.systers.suporte;

import dev.team.systers.usuarios.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Representa uma denúncia feita por um usuário.
 * contém informações sobre o autor, o usuário reportado e o status da denúncia.
 */
@Entity
@Table(name = "denuncia")
public class Denuncia {

    /**
     * Identificador único da denúncia.
     * Obrigatório (não há denúncia sem ID).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "denuncia_id", nullable = false)
    private Long id; // Chave primária da denúncia

    /**
     * Descrição detalhada da denúncia.
     */
    @Column(name = "denuncia_descricao", nullable = false)
    private String descricao;

    /**
     * Categoria da denúncia (Exemplo: "Comportamento Inapropriado", "Assédio", etc.).
     */
    @Column(name = "denuncia_categoria", nullable = false)
    private String categoria;

    /**
     * Status da denúncia (Exemplo: "Em análise", "Resolvida", "Fechada").
     */
    @Column(name = "denuncia_status", nullable = false)
    private String status;

    /**
     * Data e hora em que a denúncia foi registrada.
     */
    @Column(name = "denuncia_data_hora", nullable = false)
    private LocalDateTime dataHora; // Usando LocalDateTime para armazenar data e hora

    /**
     * Associação do usuário autor da denúncia.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_autor_fk", referencedColumnName = "id", foreignKey = @ForeignKey(name = "usuario_autor_fk"), nullable = false)
    private Usuario usuarioAutor; // Relacionamento com o autor da denúncia (Usuário que fez a denúncia)

    /**
     * Associação do usuário reportado na denúncia.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_reportado_fk", referencedColumnName = "id", foreignKey = @ForeignKey(name = "usuario_reportado_fk"), nullable = false)
    private Usuario usuarioReportado; // Relacionamento com o usuário reportado (Usuário que está sendo denunciado)

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
}
