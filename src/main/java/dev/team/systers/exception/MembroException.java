package dev.team.systers.exception;

/**
 * Exceção personalizada para operações relacionadas a membros.
 * Lançada quando ocorrem erros específicos no gerenciamento de membros em grupos,
 * como problemas de permissões, status de acesso ou participação.
 */
public class MembroException extends RuntimeException {
    /**
     * Construtor que recebe a mensagem de erro.
     * @param message Descrição detalhada do erro ocorrido
     */
    public MembroException(String message) {
        super(message);
    }
}
