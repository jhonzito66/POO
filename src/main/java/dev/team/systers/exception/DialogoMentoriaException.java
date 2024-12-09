package dev.team.systers.exception;

/**
 * Exceção personalizada para operações relacionadas a diálogos de mentoria.
 * Lançada quando ocorrem erros específicos na comunicação durante mentorias,
 * como problemas no envio de mensagens ou violações de regras de interação.
 */
public class DialogoMentoriaException extends RuntimeException {
    /**
     * Construtor que recebe a mensagem de erro.
     * @param message Descrição detalhada do erro ocorrido
     */
    public DialogoMentoriaException(String message) {
        super(message);
    }
}
