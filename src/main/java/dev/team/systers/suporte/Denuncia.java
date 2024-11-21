package dev.team.systers.suporte;

import dev.team.systers.usuarios.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/*
Exemplo: Usuária faz uma Denuncia sobre comportamento inapropriado, que é analisada por uma Moderadora.

Denuncia: Registra e gerencia denúncias com atributos id, descricao, usuarioReportado, status.
Denuncia: Fornece assistência às usuárias.
Banco de Dados: Tabelas para Denuncias, Usuarios.
 */

/**
 * Representa uma denúncia.
 * Possui usuário autor e usuário reportado.
 */
@Entity
@Table(name = "denuncia")
public class Denuncia {
    /**
     * Identificador único da denúncia.
     * Obrigatório (não há denúncia sem ID).
     */
    @Id
    @Column(name = "denuncia_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk
    // todo: concluir o restante (nomear as colunas de acordo com o DER
    private String descricao;
    private String categoria;
    private String status;
    private LocalDateTime dataDenuncia;

    /**
     * Associação do usuário autor da denúncia.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_autor_fk", foreignKey = @ForeignKey(name = "usuario_autor_fk"), nullable = false)
    private Usuario usuarioAutor;

    /**
     * Associação do usuário reportado na denúncia.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_reportado_fk", foreignKey = @ForeignKey(name = "usuario_reportado_fk"), nullable = false)
    private Usuario usuarioReportado;
}
