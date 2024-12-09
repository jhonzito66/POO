package dev.team.systers.tools;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.team.systers.exception.ComentarioException;
import dev.team.systers.exception.DenunciaException;
import dev.team.systers.exception.DialogoMentoriaException;
import dev.team.systers.exception.GrupoException;
import dev.team.systers.exception.MembroException;
import dev.team.systers.exception.MentoriaException;
import dev.team.systers.exception.PostagemException;
import dev.team.systers.exception.UsuarioException;

/**
 * Controlador global de exceções para a aplicação.
 * Esta classe é responsável por capturar e tratar exceções lançadas em qualquer parte da aplicação,
 * fornecendo respostas HTTP apropriadas e mensagens de erro consistentes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de argumentos inválidos.
     * Retorna status 400 (Bad Request) para dados inválidos ou malformados.
     * @param ex A exceção de argumento inválido
     * @return Resposta com mensagem de erro
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Trata exceções de tempo de execução.
     * Retorna status 500 (Internal Server Error) para erros inesperados.
     * @param ex A exceção de runtime
     * @return Resposta com mensagem genérica
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocorreu um erro inesperado. Tente novamente mais tarde.");
    }

    /**
     * Trata exceções genéricas não especificadas.
     * Retorna status 500 (Internal Server Error) para qualquer erro não tratado.
     * @param ex A exceção genérica
     * @return Resposta com mensagem genérica
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocorreu um erro inesperado. Tente novamente mais tarde.");
    }

    /**
     * Trata exceções específicas de usuário.
     * Retorna status 404 (Not Found) para recursos de usuário não encontrados.
     * @param ex A exceção de usuário
     * @return Resposta com mensagem específica
     */
    @ExceptionHandler(UsuarioException.class)
    public ResponseEntity<String> handleUsuarioException(UsuarioException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Trata exceções específicas de grupo.
     * Retorna status 400 (Bad Request) para operações inválidas em grupos.
     * @param ex A exceção de grupo
     * @return Resposta com mensagem específica
     */
    @ExceptionHandler(GrupoException.class)
    public ResponseEntity<String> handleGrupoException(GrupoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Trata exceções específicas de postagem.
     * Retorna status 400 (Bad Request) para operações inválidas em postagens.
     * @param ex A exceção de postagem
     * @return Resposta com mensagem específica
     */
    @ExceptionHandler(PostagemException.class)
    public ResponseEntity<String> handlePostagemException(PostagemException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Trata exceções específicas de membro.
     * Retorna status 400 (Bad Request) para operações inválidas com membros.
     * @param ex A exceção de membro
     * @return Resposta com mensagem específica
     */
    @ExceptionHandler(MembroException.class)
    public ResponseEntity<String> handleMembroException(MembroException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Trata exceções específicas de comentário.
     * Retorna status 400 (Bad Request) para operações inválidas em comentários.
     * @param ex A exceção de comentário
     * @return Resposta com mensagem específica
     */
    @ExceptionHandler(ComentarioException.class)
    public ResponseEntity<String> handleComentarioException(ComentarioException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Trata exceções específicas de mentoria.
     * Retorna status 400 (Bad Request) para operações inválidas em mentorias.
     * @param ex A exceção de mentoria
     * @return Resposta com mensagem específica
     */
    @ExceptionHandler(MentoriaException.class)
    public ResponseEntity<String> handleMentoriaException(MentoriaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Trata exceções específicas de diálogo de mentoria.
     * Retorna status 400 (Bad Request) para operações inválidas em diálogos.
     * @param ex A exceção de diálogo
     * @return Resposta com mensagem específica
     */
    @ExceptionHandler(DialogoMentoriaException.class)
    public ResponseEntity<String> handleDialogoMentoriaException(DialogoMentoriaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Trata exceções específicas de denúncia.
     * Retorna status 400 (Bad Request) para operações inválidas em denúncias.
     * @param ex A exceção de denúncia
     * @return Resposta com mensagem específica
     */
    @ExceptionHandler(DenunciaException.class)
    public ResponseEntity<String> handleDenunciaException(DenunciaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}