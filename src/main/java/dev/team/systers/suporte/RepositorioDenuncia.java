package dev.team.systers.suporte;

import dev.team.systers.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    // Buscar denúncias por categoria
    List<Denuncia> findByCategoria(String categoria);

    // Buscar denúncias por status
    List<Denuncia> findByStatus(String status);

    // Buscar denúncias feitas por um usuário específico
    List<Denuncia> findByUsuarioAutor(Usuario usuarioAutor);

    // Buscar denúncias contra um usuário específico
    List<Denuncia> findByUsuarioReportado(Usuario usuarioReportado);

    // Buscar denúncias feitas em um intervalo de tempo
    List<Denuncia> findByDataHoraBetween(LocalDateTime start, LocalDateTime end);
}

