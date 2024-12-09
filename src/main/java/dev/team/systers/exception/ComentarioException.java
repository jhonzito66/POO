package dev.team.systers.exception;

/**
 * Exceção personalizada para operações relacionadas a comentários.
 * Lançada quando ocorrem erros específicos no gerenciamento de comentários em postagens,
 * como problemas de criação, edição ou moderação de conteúdo.
 */
public class ComentarioException extends RuntimeException {
    /**
     * Construtor que recebe a mensagem de erro.
     * @param message Descrição detalhada do erro ocorrido
     */
    public ComentarioException(String message) {
        super(message);
    }
}
