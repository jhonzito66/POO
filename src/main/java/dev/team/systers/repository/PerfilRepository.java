package dev.team.systers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Perfil findPerfilByUsuarioPerfil_Id(Long usuarioPerfilId);
}
