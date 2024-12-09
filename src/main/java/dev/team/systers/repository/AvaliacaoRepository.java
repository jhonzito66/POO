package dev.team.systers.repository;

import dev.team.systers.model.Avaliacao;
import dev.team.systers.model.Mentoria;
import dev.team.systers.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    boolean existsByMentoriaAvaliadaAndParticipanteAvaliador(Mentoria mentoria, Usuario participanteAvaliador);

    boolean existsByMentoriaAvaliadaIdAndParticipanteAvaliadorId(Long mentoriaId, Long usuarioId);

    Avaliacao findByMentoriaAvaliadaId(Long mentoriaId);
}
