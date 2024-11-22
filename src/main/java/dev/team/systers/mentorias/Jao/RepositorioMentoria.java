package dev.team.systers.mentorias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MentoriaRepository extends JpaRepository<Mentoria, Long> {

    // Encontrar mentorias por nome
    List<Mentoria> findByNome(String nome);

    // Encontrar mentorias por status
    List<Mentoria> findByStatus(String status);

    // Encontrar mentorias de um determinado mentor
    List<Mentoria> findByMentor(Mentor mentor);

    // Encontrar mentorias de um determinado mentee
    List<Mentoria> findByMentee(Mentee mentee);

    // Encontrar mentorias que estão no intervalo de uma data
    List<Mentoria> findByDataHoraInicioBetween(LocalDateTime start, LocalDateTime end);
}
