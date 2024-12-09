package dev.team.systers.exception;

/**
 * Exceção personalizada para operações relacionadas a postagens.
 * Lançada quando ocorrem erros específicos no gerenciamento de postagens em grupos,
 * como problemas de criação, edição ou remoção de conteúdo.
 */
public class PostagemException extends RuntimeException {
    /**
     * Construtor que recebe a mensagem de erro.
     * @param message Descrição detalhada do erro ocorrido
     */
    public PostagemException(String message) {
        super(message);
    }
}
