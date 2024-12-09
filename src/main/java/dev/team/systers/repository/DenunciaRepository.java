package dev.team.systers.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Denuncia;
import dev.team.systers.model.Denuncia.StatusDenuncia;

/**
 * Repositório para operações de persistência de Denúncia.
 * Fornece métodos para acessar e manipular dados de denúncias no banco de dados.
 */
@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    /**
     * Busca denúncias por status.
     * @param status Status da denúncia (PENDENTE ou ATENDIDA)
     * @return Lista de denúncias com o status especificado
     */
    List<Denuncia> findByStatus(StatusDenuncia status);

    /**
     * Busca denúncias por categoria.
     * @param categoria Categoria da denúncia
     * @return Lista de denúncias da categoria especificada
     */
    List<Denuncia> findByCategoria(String categoria);

    /**
     * Busca denúncias feitas por um usuário específico.
     * @param usuarioId ID do usuário autor
     * @return Lista de denúncias feitas pelo usuário
     */
    List<Denuncia> findByUsuarioAutor_Id(Long usuarioId);

    /**
     * Busca denúncias feitas contra um usuário específico.
     * @param usuarioId ID do usuário reportado
     * @return Lista de denúncias contra o usuário
     */
    List<Denuncia> findByUsuarioReportado_Id(Long usuarioId);

    /**
     * Busca denúncias feitas após uma data específica.
     * @param dataHora Data e hora de referência
     * @return Lista de denúncias posteriores à data
     */
    List<Denuncia> findByDataHoraAfter(LocalDateTime dataHora);

    /**
     * Busca denúncias dentro de um intervalo de datas.
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de denúncias no período
     */
    List<Denuncia> findByDataHoraBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca denúncias por categoria e status.
     * @param categoria Categoria da denúncia
     * @param status Status da denúncia
     * @return Lista de denúncias que atendem aos critérios
     */
    List<Denuncia> findByCategoriaAndStatus(String categoria, String status);

    /**
     * Busca denúncias pendentes contra um usuário específico.
     * @param usuarioId ID do usuário reportado
     * @return Lista de denúncias pendentes contra o usuário
     */
    @Query("SELECT d FROM Denuncia d WHERE d.usuarioReportado.id = :usuarioId AND d.status = 'PENDENTE'")
    List<Denuncia> findPendingByReportedUser(@Param("usuarioId") Long usuarioId);

    /**
     * Conta o número de denúncias pendentes contra um usuário.
     * @param usuarioId ID do usuário reportado
     * @return Quantidade de denúncias pendentes
     */
    @Query("SELECT COUNT(d) FROM Denuncia d WHERE d.usuarioReportado.id = :usuarioId AND d.status = 'PENDENTE'")
    long countActiveReportsByUser(@Param("usuarioId") Long usuarioId);

    /**
     * Busca todas as denúncias com dados dos usuários relacionados.
     * Utiliza FETCH JOIN para otimizar o carregamento.
     * @return Lista de denúncias com dados completos
     */
    @Query("SELECT d FROM Denuncia d " +
           "LEFT JOIN FETCH d.usuarioAutor " +
           "LEFT JOIN FETCH d.usuarioReportado")
    List<Denuncia> findAllWithUsuarios();

    /**
     * Busca denúncias feitas por um usuário com dados relacionados.
     * @param autorId ID do usuário autor
     * @return Lista de denúncias com dados completos
     */
    @Query("SELECT d FROM Denuncia d LEFT JOIN FETCH d.usuarioReportado LEFT JOIN FETCH d.usuarioAutor WHERE d.usuarioAutor.id = :autorId")
    List<Denuncia> findByUsuarioAutorIdWithUsuarios(@Param("autorId") Long autorId);

    /**
     * Busca denúncias por status com dados dos usuários relacionados.
     * @param status Status da denúncia
     * @return Lista de denúncias com dados completos
     */
    @Query("SELECT d FROM Denuncia d LEFT JOIN FETCH d.usuarioReportado LEFT JOIN FETCH d.usuarioAutor WHERE d.status = :status")
    List<Denuncia> findByStatusWithUsuarios(@Param("status") StatusDenuncia status);
}
