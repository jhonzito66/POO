package dev.team.systers.tools;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.team.systers.grupos.GrupoException;

/**
 * Controlador global de exceções para a aplicação.
 * <p>
 * Esta classe é responsável por capturar e tratar exceções lançadas em qualquer parte da aplicação,
 * permitindo que mensagens de erro apropriadas sejam retornadas ao cliente.
 * <p>
 * As exceções tratadas incluem:
 * - IllegalArgumentException
 * - RuntimeException
 * - GrupoException
 * - Exception (exceção genérica)
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções do tipo IllegalArgumentException.
     * 
     * @param ex A exceção lançada.
     * @return Uma resposta com status 400 (Bad Request) e a mensagem da exceção.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Trata exceções do tipo RuntimeException.
     * 
     * @param ex A exceção lançada.
     * @return Uma resposta com status 500 (Internal Server Error) e uma mensagem genérica de erro.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro inesperado. Tente novamente mais tarde.");
    }

    /**
     * Trata exceções do tipo GrupoException.
     * 
     * @param ex A exceção lançada.
     * @return Uma resposta com status 400 (Bad Request) e a mensagem da exceção.
     */
    @ExceptionHandler(GrupoException.class)
    public ResponseEntity<String> handleGrupoException(GrupoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Trata exceções genéricas.
     * 
     * @param ex A exceção lançada.
     * @return Uma resposta com status 500 (Internal Server Error) e uma mensagem genérica de erro.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro inesperado. Tente novamente mais tarde.");
    }

    /**
     * Exemplo de como criar e implementar uma nova exceção personalizada.
     * 
     * 1. Crie uma nova classe de exceção que estenda a classe Exception ou RuntimeException.
     * 
     * Exemplo:
     * public class MinhaNovaExcecao extends RuntimeException {
     *     public MinhaNovaExcecao(String message) {
     *         super(message);
     *     }
     * }
     * 
     * 2. Adicione um novo metodo de tratamento na classe GlobalExceptionHandler.
     * 
     * Exemplo:
     * @ExceptionHandler(MinhaNovaExcecao.class)
     * public ResponseEntity<String> handleMinhaNovaExcecao(MinhaNovaExcecao ex) {
     *     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
     * }
     * 
     * <p>
     * Tipos comuns de exceções e quando geralmente são usadas:
     * 
     * 1. IllegalArgumentException:
     *    - Usada quando um metodo recebe um argumento inválido.
     *    - Exemplo: Passar um valor nulo ou fora do intervalo esperado.
     * 
     * 2. NullPointerException:
     *    - Usada quando uma operação é realizada em uma referência nula.
     *    - Exemplo: Tentar acessar um metodo ou propriedade de um objeto que não foi inicializado.
     * 
     * 3. RuntimeException:
     *    - Classe base para exceções que podem ocorrer durante a execução do programa.
     *    - Usada para indicar erros que não são esperados e que não podem ser recuperados.
     * 
     * 4. Exception:
     *    - Classe base para todas as exceções.
     *    - Usada para capturar erros genéricos que não se enquadram em outras categorias.
     * 
     * <p>
     * Status HTTP mais usados para erros:
     * 
     * 1. 400 Bad Request:
     *    - Indica que a solicitação do cliente contém dados inválidos ou malformados.
     *    - Usado frequentemente com IllegalArgumentException e GrupoException.
     * 
     * 2. 404 Not Found:
     *    - Indica que o recurso solicitado não foi encontrado no servidor.
     *    - Usado quando um endpoint não corresponde a nenhum recurso existente.
     * 
     * 3. 500 Internal Server Error:
     *    - Indica que ocorreu um erro inesperado no servidor.
     *    - Usado frequentemente com RuntimeException e Exception genérica.
     * 
     * 4. 403 Forbidden:
     *    - Indica que o servidor entendeu a solicitação, mas se recusa a autorizá-la.
     *    - Usado em casos de falta de permissões para acessar um recurso.
     * 
     * 5. 401 Unauthorized:
     *    - Indica que a autenticação é necessária e falhou ou não foi fornecida.
     *    - Usado quando o usuário não está autenticado para acessar um recurso protegido.
     * 
     * <p>
     * Quando uma nova exceção personalizada é lançada em qualquer parte da aplicação, 
     * o Spring irá automaticamente capturá-la e direcioná-la para o metodo de tratamento
     * correspondente na classe GlobalExceptionHandler. Isso permite que você forneça 
     * respostas apropriadas e consistentes para o cliente, melhorando a experiência do usuário 
     * e facilitando a depuração.
     */
}