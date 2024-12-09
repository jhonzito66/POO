package dev.team.systers.exception;

/**
 * Exceção personalizada para operações relacionadas a denúncias.
 * Lançada quando ocorrem erros específicos no processamento de denúncias,
 * como validações de dados ou violações de regras de negócio.
 */
public class DenunciaException extends RuntimeException {
    /**
     * Construtor que recebe a mensagem de erro.
     * @param message Descrição detalhada do erro ocorrido
     */
    public DenunciaException(String message) {
        super(message);
    }
}
