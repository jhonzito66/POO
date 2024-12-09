package dev.team.systers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Grupo;
import dev.team.systers.model.Membro;
import dev.team.systers.model.Usuario;

/**
 * Repositório para operações de persistência de Membro.
 * Fornece métodos para acessar e manipular dados de membros em grupos.
 */
@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {
    
    /**
     * Busca um membro específico pela combinação de usuário e grupo.
     * @param usuario Usuário a ser buscado
     * @param grupo Grupo a ser buscado
     * @return Membro encontrado ou vazio se não existir
     */
    Optional<Membro> findByUsuarioAndGrupo(Usuario usuario, Grupo grupo);

    /**
     * Verifica se um usuário é membro de um grupo específico.
     * @param usuario Usuário a ser verificado
     * @param grupo Grupo a ser verificado
     * @return true se o usuário é membro, false caso contrário
     */
    boolean existsByUsuarioAndGrupo(Usuario usuario, Grupo grupo);

    /**
     * Busca todos os membros de um grupo específico.
     * @param grupo Grupo para buscar os membros
     * @return Lista de membros do grupo
     */
    List<Membro> findByGrupo(Grupo grupo);

    /**
     * Busca todos os grupos dos quais um usuário é membro.
     * @param usuario Usuário para buscar as participações
     * @return Lista de participações do usuário em grupos
     */
    List<Membro> findByUsuario(Usuario usuario);

    /**
     * Busca um membro específico usando IDs de usuário e grupo.
     * @param id ID do usuário
     * @param grupo ID do grupo
     * @return Membro encontrado ou null se não existir
     */
    Membro findByUsuarioIdAndGrupoId(Long id, Long grupo);
}
