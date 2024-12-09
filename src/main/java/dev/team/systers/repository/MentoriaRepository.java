package dev.team.systers.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Mentoria;

/**
 * Repositório para operações de persistência de Mentoria.
 * Fornece métodos para acessar e manipular dados de mentorias no banco de dados.
 */
@Repository
public interface MentoriaRepository extends JpaRepository<Mentoria, Long> {

    /**
     * Busca mentorias por status.
     * @param status Status da mentoria (ex: Agendada, Em Andamento, Concluída)
     * @return Lista de mentorias com o status especificado
     */
    List<Mentoria> findByStatus(String status);

    /**
     * Busca mentorias por nome, ignorando maiúsculas e minúsculas.
     * @param nome Texto a ser buscado no nome da mentoria
     * @return Lista de mentorias que contêm o texto no nome
     */
    List<Mentoria> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca mentorias que começam após uma data específica.
     * @param dataHoraInicio Data e hora de referência
     * @return Lista de mentorias futuras à data especificada
     */
    List<Mentoria> findByDataHoraInicioAfter(LocalDateTime dataHoraInicio);

    /**
     * Busca mentorias que terminam antes de uma data específica.
     * @param dataHoraFim Data e hora de referência
     * @return Lista de mentorias que terminam antes da data
     */
    List<Mentoria> findByDataHoraFimBefore(LocalDateTime dataHoraFim);

    /**
     * Busca mentorias em um intervalo de datas.
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de mentorias no período especificado
     */
    List<Mentoria> findByDataHoraInicioBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca mentorias de um participante específico.
     * @param participanteId ID do participante (mentor ou mentorado)
     * @return Lista de mentorias do participante
     */
    List<Mentoria> findByParticipantes_Id(Long participanteId);

    /**
     * Busca uma mentoria pelo seu ID.
     * @param id ID da mentoria
     * @return Mentoria encontrada ou null se não existir
     */
    Mentoria findMentoriaById(Long id);
}
