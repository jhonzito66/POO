package dev.team.systers.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.model.Grupo;
import dev.team.systers.model.Perfil;
import dev.team.systers.model.Usuario;
import dev.team.systers.repository.GrupoRepository;
import dev.team.systers.repository.PerfilRepository;
import dev.team.systers.repository.UsuarioRepository;

/**
 * Serviço responsável pelo gerenciamento de perfis de usuários no sistema.
 * Fornece funcionalidades para busca, atualização e gerenciamento de perfis
 * e suas relações com grupos e usuários.
 */
@Service
public class PerfilService {
    
    /**
     * Repositório para acesso aos dados de perfis.
     */
    private final PerfilRepository perfilRepository;

    /**
     * Repositório para acesso aos dados de usuários.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Repositório para acesso aos dados de grupos.
     */
    private final GrupoRepository grupoRepository;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     * @param perfilRepository Repositório de perfis injetado pelo Spring
     * @param usuarioRepository Repositório de usuários injetado pelo Spring
     * @param grupoRepository Repositório de grupos injetado pelo Spring
     */
    @Autowired
    public PerfilService(PerfilRepository perfilRepository, UsuarioRepository usuarioRepository, GrupoRepository grupoRepository) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
    }

    /**
     * Busca o perfil de um usuário específico pelo ID.
     * 
     * @param idUsuario ID do usuário cujo perfil será buscado
     * @return Perfil do usuário ou null se não encontrado
     */
    public Perfil buscarPerfilPorIDUsuario(Long idUsuario) {
        return perfilRepository.findById(idUsuario).orElse(null);
    }

    /**
     * Busca todos os grupos dos quais um usuário participa.
     * 
     * @param perfilId ID do perfil do usuário
     * @return Lista de grupos dos quais o usuário participa
     * @throws IllegalArgumentException se o perfil ou grupo não for encontrado
     */
    public List<Grupo> buscarGruposDoUsuario(Long perfilId) {
        // Primeiro, busque o perfil pelo ID
        Perfil perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado com o ID: " + perfilId));

        // Obtenha o usuário associado ao perfil
        Usuario usuario = perfil.getUsuarioPerfil();

        // Agora, busque os grupos que o usuário pertence
        List<Object[]> gruposIds = usuarioRepository.findGruposDeUmUsuarioNativo(usuario.getId());

        // Converta os IDs dos grupos em objetos Grupo
        List<Grupo> grupos = new ArrayList<>();
        for (Object[] grupoId : gruposIds) {
            Long id = (Long) grupoId[0];
            Grupo grupo = grupoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado com o ID: " + id));
            grupos.add(grupo);
        }

        return grupos;
    }

    /**
     * Atualiza as informações de um perfil existente.
     * Permite atualizar o nome e a biografia do perfil.
     * 
     * @param perfilUpdate Objeto contendo as novas informações do perfil
     * @throws IllegalArgumentException se o perfil não for encontrado
     */
    public void update(Perfil perfilUpdate) {
        Perfil existingPerfil = perfilRepository.findById(perfilUpdate.getId())
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado para o usuário com ID: " + perfilUpdate.getId()));

        if (perfilUpdate.getPerfilNome() != null) {
            existingPerfil.setPerfilNome(perfilUpdate.getPerfilNome());
        }
        if (perfilUpdate.getPerfilBio() != null) {
            existingPerfil.setPerfilBio(perfilUpdate.getPerfilBio());
        }

        perfilRepository.save(existingPerfil);
    }
}
