package dev.team.systers.model.suporte;

/*
Exemplo: Usuária faz uma Denuncia sobre comportamento inapropriado, que é analisada por uma Moderadora.

Denuncia: Registra e gerencia denúncias com atributos id, descricao, usuarioReportado, status.
Denuncia: Fornece assistência às usuárias.
Banco de Dados: Tabelas para Denuncias, Usuarios.
 */
public class Denuncia {
    private Long id; // pk
    private String descricao;

    // usuário reportado e usuário autor da denúncia
    // denuncia tipo

    private Enum<Status> status;

    private enum Status {
            ABERTA,
            ANALISE,
            FECHADA
    }
}
