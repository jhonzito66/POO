package dev.team.systers.exception;

/**
 * Exceção personalizada para operações relacionadas a mentorias.
 * Lançada quando ocorrem erros específicos no gerenciamento de sessões de mentoria,
 * como problemas de agendamento, participação ou avaliação.
 */
public class MentoriaException extends RuntimeException {
    /**
     * Construtor que recebe a mensagem de erro.
     * @param message Descrição detalhada do erro ocorrido
     */
    public MentoriaException(String message) {
        super(message);
    }
}
