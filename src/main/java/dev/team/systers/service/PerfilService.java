package dev.team.systers.service;

import java.util.ArrayList;
import java.util.List;

import dev.team.systers.model.Perfil;
import dev.team.systers.model.Usuario;
import dev.team.systers.repository.GrupoRepository;
import dev.team.systers.repository.PerfilRepository;
import dev.team.systers.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.model.Grupo;

@Service
public class PerfilService {
    private PerfilRepository perfilRepository;
    private UsuarioRepository usuarioRepository;
    private GrupoRepository grupoRepository;

    @Autowired
    public PerfilService(PerfilRepository perfilRepository, UsuarioRepository usuarioRepository, GrupoRepository grupoRepository) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
    }

    public List<Grupo> buscarGruposDoUsuario(Long perfilId) {
        // Primeiro, busque o perfil pelo ID
        Perfil perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado com o ID: " + perfilId));

        // Obtenha o usuário associado ao perfil
        Usuario usuario = perfil.getUsuario_perfil();

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
}
