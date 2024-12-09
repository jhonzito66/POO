package dev.team.systers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Avaliacao;
import dev.team.systers.model.Mentoria;
import dev.team.systers.model.Usuario;

/**
 * Repositório para operações de persistência de Avaliação.
 * Fornece métodos para acessar e manipular dados de avaliações de mentorias.
 */
@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    /**
     * Verifica se existe avaliação de um participante para uma mentoria.
     * @param mentoria Mentoria avaliada
     * @param participanteAvaliador Usuário que avalia
     * @return true se a avaliação existe, false caso contrário
     */
    boolean existsByMentoriaAvaliadaAndParticipanteAvaliador(Mentoria mentoria, Usuario participanteAvaliador);

    /**
     * Verifica se existe avaliação usando IDs de mentoria e usuário.
     * @param mentoriaId ID da mentoria
     * @param usuarioId ID do usuário avaliador
     * @return true se a avaliação existe, false caso contrário
     */
    boolean existsByMentoriaAvaliadaIdAndParticipanteAvaliadorId(Long mentoriaId, Long usuarioId);

    /**
     * Busca avaliação de uma mentoria específica.
     * @param mentoriaId ID da mentoria
     * @return Avaliação encontrada ou null se não existir
     */
    Avaliacao findByMentoriaAvaliadaId(Long mentoriaId);
}
