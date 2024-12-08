package dev.team.systers.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Denuncia;
import dev.team.systers.model.Denuncia.StatusDenuncia;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    // Buscar denúncias por status
    List<Denuncia> findByStatus(StatusDenuncia status);

    // Buscar denúncias por categoria
    List<Denuncia> findByCategoria(String categoria);

    // Buscar denúncias feitas por um usuário específico
    List<Denuncia> findByUsuarioAutor_Id(Long usuarioId);

    // Buscar denúncias reportadas contra um usuário específico
    List<Denuncia> findByUsuarioReportado_Id(Long usuarioId);

    // Buscar denúncias feitas após uma data específica
    List<Denuncia> findByDataHoraAfter(LocalDateTime dataHora);

    // Buscar denúncias dentro de um intervalo de datas
    List<Denuncia> findByDataHoraBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Buscar denúncias de uma categoria e com status específico
    List<Denuncia> findByCategoriaAndStatus(String categoria, String status);

    // Novos métodos úteis
    @Query("SELECT d FROM Denuncia d WHERE d.usuarioReportado.id = :usuarioId AND d.status = 'PENDENTE'")
    List<Denuncia> findPendingByReportedUser(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COUNT(d) FROM Denuncia d WHERE d.usuarioReportado.id = :usuarioId AND d.status = 'PENDENTE'")
    long countActiveReportsByUser(@Param("usuarioId") Long usuarioId);

    @Query("SELECT d FROM Denuncia d " +
           "LEFT JOIN FETCH d.usuarioAutor " +
           "LEFT JOIN FETCH d.usuarioReportado")
    List<Denuncia> findAllWithUsuarios();

    @Query("SELECT d FROM Denuncia d " +
           "LEFT JOIN FETCH d.usuarioAutor " +
           "LEFT JOIN FETCH d.usuarioReportado " +
           "WHERE d.usuarioAutor.id = :usuarioId")
    List<Denuncia> findByUsuarioAutorIdWithUsuarios(@Param("usuarioId") Long usuarioId);
}
