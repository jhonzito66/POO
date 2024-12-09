package dev.team.systers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Mentoria;
import dev.team.systers.model.Participante;
import dev.team.systers.model.Usuario;

/**
 * Repositório para operações de persistência de Participante.
 * Fornece métodos para acessar e manipular dados de participantes de mentorias.
 */
@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    
    /**
     * Busca participantes por ID do usuário.
     * @param id ID do usuário
     * @return Lista de participações do usuário
     */
    List<Participante> findByUsuarioId(Long id);

    /**
     * Busca participantes por usuário e tipo (mentor/mentorado).
     * @param usuario_id ID do usuário
     * @param tipo Tipo de participação
     * @return Lista de participações do usuário com o tipo especificado
     */
    List<Participante> findByUsuarioIdAndTipo(Long usuario_id, Participante.TipoParticipante tipo);

    /**
     * Busca participação específica de um usuário em uma mentoria.
     * @param usuario Usuário participante
     * @param mentoria Mentoria específica
     * @return Participação encontrada ou null se não existir
     */
    Participante findParticipanteByUsuarioAndMentoria(Usuario usuario, Mentoria mentoria);
}