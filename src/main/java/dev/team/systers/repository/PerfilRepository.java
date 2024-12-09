package dev.team.systers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Perfil;

/**
 * Repositório para operações de persistência de Perfil.
 * Fornece métodos para acessar e manipular dados de perfis de usuários.
 */
@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    
    /**
     * Busca o perfil de um usuário específico.
     * @param usuarioPerfilId ID do usuário
     * @return Perfil encontrado ou null se não existir
     */
    Perfil findPerfilByUsuarioPerfil_Id(Long usuarioPerfilId);
}
