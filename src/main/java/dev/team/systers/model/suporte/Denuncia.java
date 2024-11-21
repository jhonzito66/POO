package dev.team.systers.model.suporte;

import java.time.LocalDateTime;

/*
Exemplo: Usuária faz uma Denuncia sobre comportamento inapropriado, que é analisada por uma Moderadora.

Denuncia: Registra e gerencia denúncias com atributos id, descricao, usuarioReportado, status.
Denuncia: Fornece assistência às usuárias.
Banco de Dados: Tabelas para Denuncias, Usuarios.
 */
public class Denuncia {
    private Long id; // pk
    private String descricao;
    private String categoria;
    private String status;
    private LocalDateTime dataDenuncia;

    // usuário reportado e usuário autor da denúncia


}
