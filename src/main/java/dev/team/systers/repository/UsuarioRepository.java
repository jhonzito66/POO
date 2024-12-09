package dev.team.systers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.team.systers.model.Usuario;

/**
 * Repositório para operações de persistência de Usuário.
 * Fornece métodos para acessar e manipular dados de usuários no banco de dados.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca todos os grupos aos quais um usuário pertence.
     * Utiliza SQL nativo para otimizar a consulta de relacionamentos.
     * 
     * @param usuarioId ID do usuário para buscar os grupos
     * @return Lista de IDs dos grupos do usuário
     */
    @Query(value = "SELECT grupo_id " +
            "FROM grupo " +
            "JOIN membro ON grupo_id = grupo_id_membro_fk " +
            "JOIN usuario ON membro_id = :usuarioId",
            nativeQuery = true)
    List<Object[]> findGruposDeUmUsuarioNativo(@Param("usuarioId") Long usuarioId);

    /**
     * Busca um usuário pelo seu login.
     * 
     * @param login Login do usuário
     * @return Usuário encontrado ou vazio se não existir
     */
    Optional<Usuario> findByLogin(String login);

    /**
     * Busca usuários que possuem denúncias pendentes.
     * Utiliza FETCH JOIN para otimizar o carregamento das denúncias.
     * 
     * @return Lista de usuários com denúncias pendentes
     */
    @Query("SELECT DISTINCT u FROM Usuario u " +
           "JOIN FETCH u.denunciasRecebidas d " +
           "WHERE d.status = dev.team.systers.model.Denuncia$StatusDenuncia.PENDENTE")
    List<Usuario> findUsuariosComDenuncias();

    /**
     * Busca um usuário pelo seu login.
     * Método alternativo que retorna o usuário diretamente.
     * 
     * @param name Login do usuário
     * @return Usuário encontrado ou null se não existir
     */
    Usuario findUsuarioByLogin(String name);
}
