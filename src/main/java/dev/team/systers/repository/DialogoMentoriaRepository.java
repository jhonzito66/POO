package dev.team.systers.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.DialogoMentoria;

/**
 * Repositório para operações de persistência de Diálogo de Mentoria.
 * Fornece métodos para acessar e manipular dados de mensagens trocadas durante mentorias.
 */
@Repository
public interface DialogoMentoriaRepository extends JpaRepository<DialogoMentoria, Long> {

    /**
     * Busca diálogos de um participante específico.
     * @param participanteId ID do participante
     * @return Lista de diálogos do participante
     */
    List<DialogoMentoria> findByParticipante_Id(Long participanteId);

    /**
     * Busca diálogos de uma mentoria específica.
     * @param mentoriaId ID da mentoria
     * @return Lista de diálogos da mentoria
     */
    List<DialogoMentoria> findByMentoria_Id(Long mentoriaId);

    /**
     * Busca diálogos posteriores a uma data específica.
     * @param dataHora Data e hora de referência
     * @return Lista de diálogos após a data especificada
     */
    List<DialogoMentoria> findByDataHoraAfter(LocalDateTime dataHora);

    /**
     * Busca diálogos dentro de um intervalo de datas.
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de diálogos no período especificado
     */
    List<DialogoMentoria> findByDataHoraBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca diálogos de uma mentoria que contenham determinado texto.
     * Realiza busca case-insensitive com correspondência parcial.
     * @param mentoriaId ID da mentoria
     * @param mensagem Texto a ser buscado nas mensagens
     * @return Lista de diálogos que contêm o texto
     */
    List<DialogoMentoria> findByMentoria_IdAndMensagemContainingIgnoreCase(Long mentoriaId, String mensagem);
}
