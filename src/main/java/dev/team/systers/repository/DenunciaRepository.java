package dev.team.systers.repository;

import dev.team.systers.model.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    // Buscar denúncias por status
    List<Denuncia> findByStatus(String status);

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
}
