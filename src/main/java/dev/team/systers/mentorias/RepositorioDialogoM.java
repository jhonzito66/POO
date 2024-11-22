package dev.team.systers.mentorias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DialogoMentoriaRepository extends JpaRepository<DialogoMentoria, Long> {

    // Buscar todos os diálogos de uma determinada mentoria
    List<DialogoMentoria> findByMentoria(Mentoria mentoria);

    // Buscar todos os diálogos de um determinado mentor
    List<DialogoMentoria> findByMentor(Mentor mentor);

    // Buscar todos os diálogos de um determinado mentee
    List<DialogoMentoria> findByMentee(Mentee mentee);

    // Buscar todos os diálogos de uma mentoria que ocorreram em um intervalo de tempo
    List<DialogoMentoria> findByMentoriaAndDataHoraBetween(Mentoria mentoria, LocalDateTime start, LocalDateTime end);

    // Buscar todos os diálogos enviados por um determinado mentor em um intervalo de tempo
    List<DialogoMentoria> findByMentorAndDataHoraBetween(Mentor mentor, LocalDateTime start, LocalDateTime end);

    // Buscar todos os diálogos enviados por um determinado mentee em um intervalo de tempo
    List<DialogoMentoria> findByMenteeAndDataHoraBetween(Mentee mentee, LocalDateTime start, LocalDateTime end);

    // Buscar todos os diálogos de uma mentoria, ordenados por data e hora
    List<DialogoMentoria> findByMentoriaOrderByDataHoraAsc(Mentoria mentoria);
}
