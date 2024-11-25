package dev.team.systers.mentorias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MentoriaRepository extends JpaRepository<Mentoria, Long> {

    // Buscar mentorias por status
    List<Mentoria> findByStatus(String status);

    // Buscar mentorias pelo nome (ignorando maiúsculas/minúsculas)
    List<Mentoria> findByNomeContainingIgnoreCase(String nome);

    // Buscar mentorias que começam depois de uma determinada data
    List<Mentoria> findByDataHoraInicioAfter(LocalDateTime dataHoraInicio);

    // Buscar mentorias que terminam antes de uma determinada data
    List<Mentoria> findByDataHoraFimBefore(LocalDateTime dataHoraFim);

    // Buscar mentorias em um intervalo de datas
    List<Mentoria> findByDataHoraInicioBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Buscar mentorias com participantes específicos
    List<Mentoria> findByParticipantes_Id(Long participanteId);
}
