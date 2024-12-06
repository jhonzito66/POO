package dev.team.systers.repository;

import dev.team.systers.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    List<Participante> findByUsuarioId(Long id);
    // encontrar participantes por tipo
    List<Participante> findByUsuarioIdAndTipo(Long usuario_id, Participante.TipoParticipante tipo);
}