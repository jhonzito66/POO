package dev.team.systers.mentorias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DialogoMentoriaRepository extends JpaRepository<DialogoMentoria, Long> {

    // Buscar diálogos de mentoria por participante
    List<DialogoMentoria> findByParticipante_Id(Long participanteId);

    // Buscar diálogos de uma mentoria específica
    List<DialogoMentoria> findByMentoria_Id(Long mentoriaId);

    // Buscar diálogos que ocorreram após uma data específica
    List<DialogoMentoria> findByDataHoraAfter(LocalDateTime dataHora);

    // Buscar diálogos dentro de um intervalo de datas
    List<DialogoMentoria> findByDataHoraBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Buscar diálogos de uma mentoria específicos por mensagem (com parte da mensagem)
    List<DialogoMentoria> findByMentoria_IdAndMensagemContainingIgnoreCase(Long mentoriaId, String mensagem);
}
