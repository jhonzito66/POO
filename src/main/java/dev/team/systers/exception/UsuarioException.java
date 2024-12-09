package dev.team.systers.exception;

/**
 * Exceção personalizada para operações relacionadas a usuários.
 * Lançada quando ocorrem erros específicos no gerenciamento de usuários,
 * como problemas de autenticação, autorização ou dados inválidos.
 */
public class UsuarioException extends RuntimeException {
    /**
     * Construtor que recebe a mensagem de erro.
     * @param message Descrição detalhada do erro ocorrido
     */
    public UsuarioException(String message) {
        super(message);
    }
}
