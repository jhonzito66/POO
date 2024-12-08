package dev.team.systers.controller;

import dev.team.systers.exception.UsuarioException;
import dev.team.systers.model.Perfil;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.PerfilService;
import dev.team.systers.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PerfilController {
    private final UsuarioService usuarioService;
    private final PerfilService perfilService;

    @Autowired
    public PerfilController(PerfilService perfilService, UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        this.perfilService = perfilService;
    }

    @GetMapping("/perfil/me")
    public String exibirPerfilAtual(Model model) {
        Usuario usuario = obterUsuarioLogado(usuarioService);

        if (usuario == null) {
            throw new UsuarioException("Usuário não encontrado");
        }
        Perfil perfil = perfilService.buscarPerfilPorIDUsuario(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("perfil", perfil);
        return "perfil";
    }

    static void Authentication(Model model, UsuarioService usuarioService) {
        Usuario usuario = obterUsuarioLogado(usuarioService);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        model.addAttribute("usuario", usuario);
    }

    @GetMapping("/perfil/{login}")
    public String exibirPerfilUsuario(@PathVariable String login, Model model) {
        Usuario usuario = usuarioService.findByLogin(login);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping("/perfil/editar")
    public String atualizarUsuarioEPerfil(
            @ModelAttribute("usuario") Usuario usuarioUpdate,
            @ModelAttribute("perfil") Perfil perfilUpdate,
            Model model) {

        Usuario existingUsuario = usuarioService.findById(usuarioUpdate.getId());
        Perfil existingPerfil = perfilService.buscarPerfilPorIDUsuario(existingUsuario.getId());

        if (usuarioUpdate.getNome() != null && !usuarioUpdate.getNome().isEmpty()) {
            existingUsuario.setNome(usuarioUpdate.getNome());
            existingPerfil.setPerfilNome(usuarioUpdate.getNome());
        }
        if (usuarioUpdate.getEmail() != null && !usuarioUpdate.getEmail().isEmpty()) {
            existingUsuario.setEmail(usuarioUpdate.getEmail());
        }

        if (usuarioUpdate.getSenha() != null && !usuarioUpdate.getSenha().isBlank()) {
            existingUsuario.setSenha(usuarioService.encriptarSenha(usuarioUpdate.getSenha()));
        }

        if (perfilUpdate.getPerfilNome() != null && !perfilUpdate.getPerfilNome().isEmpty()) {
            existingPerfil.setPerfilNome(perfilUpdate.getPerfilNome());
        }
        if (perfilUpdate.getPerfilBio() != null && !perfilUpdate.getPerfilBio().isEmpty()) {
            existingPerfil.setPerfilBio(perfilUpdate.getPerfilBio());
        }

        usuarioService.atualizar(existingUsuario);
        perfilService.update(existingPerfil);

        model.addAttribute("usuario", existingUsuario);
        model.addAttribute("perfil", existingPerfil);
        return "perfil";
    }

    private static Usuario obterUsuarioLogado(UsuarioService usuarioService) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getName().equals("anonymousUser")) {
            throw new IllegalStateException("Usuário não autenticado");
        }

        String login = auth.getName();
        return usuarioService.findByLogin(login);
    }
}
