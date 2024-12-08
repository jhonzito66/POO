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

    public Perfil buscarPerfilPorIDUsuario(Long idUsuario) {
        return perfilRepository.findById(idUsuario).orElse(null);
    }

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
