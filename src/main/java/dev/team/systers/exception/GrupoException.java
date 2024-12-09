package dev.team.systers.exception;

/**
 * Exceção personalizada para operações relacionadas a grupos.
 * Lançada quando ocorrem erros específicos no gerenciamento de grupos,
 * como problemas na criação, atualização ou gerenciamento de membros.
 */
public class GrupoException extends RuntimeException {
    /**
     * Construtor que recebe a mensagem de erro.
     * @param message Descrição detalhada do erro ocorrido
     */
    public GrupoException(String message) {
        super(message);
    }
}