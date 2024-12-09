package dev.team.systers.controller;

import dev.team.systers.model.Denuncia;
import dev.team.systers.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import dev.team.systers.exception.UsuarioException;
import dev.team.systers.model.Perfil;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.PerfilService;
import dev.team.systers.service.UsuarioService;

@Controller
public class PerfilController {
    private final UsuarioService usuarioService;
    private final PerfilService perfilService;
    private final DenunciaService denunciaService;

    @Autowired
    public PerfilController(PerfilService perfilService, UsuarioService usuarioService, DenunciaService denunciaService) {
        this.usuarioService = usuarioService;
        this.perfilService = perfilService;
        this.denunciaService = denunciaService;
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
        Usuario usuario = usuarioService.encontrarPorLogin(login);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        Perfil perfil = perfilService.buscarPerfilPorIDUsuario(usuario.getId());
        
        if (perfil == null) {
            throw new IllegalArgumentException("Perfil não encontrado para o usuário: " + login);
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("perfil", perfil);
        return "perfil";
    }

    @PostMapping("/perfil/editar")
    public String atualizarUsuarioEPerfil(
            @ModelAttribute("usuario") Usuario usuarioUpdate,
            @ModelAttribute("perfil") Perfil perfilUpdate,
            Model model) {

        Usuario existingUsuario = usuarioService.encontrarPorID(usuarioUpdate.getId());
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

    @PostMapping("perfil/virar-mentor")
    public String virarMentor(Model model) {
        Usuario usuario = obterUsuarioLogado(usuarioService);
        
        usuario.setTipoMentor(true);
        
        usuarioService.atualizar(usuario);
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("perfil", perfilService.buscarPerfilPorIDUsuario(usuario.getId()));
        
        return "redirect:/perfil/me";
    }

    private static Usuario obterUsuarioLogado(UsuarioService usuarioService) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getName().equals("anonymousUser")) {
            throw new IllegalStateException("Usuário não autenticado");
        }

        String login = auth.getName();
        return usuarioService.encontrarPorLogin(login);
    }
    @PostMapping("perfil/deixar-mentor")
    public String deixarMentor(Model model) {
        Usuario usuario = obterUsuarioLogado(usuarioService);

        usuario.setTipoMentor(false);

        usuarioService.atualizar(usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("perfil", perfilService.buscarPerfilPorIDUsuario(usuario.getId()));

        return "redirect:/perfil/me";
    }

    @GetMapping("/perfil/editar/{usuarioId}")
    public String exibirFormularioEditarPerfil(@PathVariable Long usuarioId, Model model) {
        try {
            Usuario usuario = usuarioService.encontrarPorID(usuarioId);
            model.addAttribute("usuario", usuario);
            return "editarPerfil";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao carregar perfil: " + e.getMessage());
            return "erro";
        }
    }

    @PostMapping("/perfil/editar/{usuarioId}")
    public String editarPerfil(@PathVariable Long usuarioId, @ModelAttribute Usuario usuarioAtualizado, Model model) {
        try {
            usuarioService.editarPerfil(usuarioId,
                    usuarioAtualizado.getNome(),
                    usuarioAtualizado.getEmail(),
                    usuarioAtualizado.getTelefone());
            return "redirect:/perfil/" + usuarioId;
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao atualizar perfil: " + e.getMessage());
            return "editarPerfil";
        }
    }

    @PostMapping("perfil/denunciar")
    public String criarDenuncia(@RequestParam String categoria,
                                @RequestParam String descricao,
                                @RequestParam String loginAutor,
                                @RequestParam String loginReportado,
                                Model model) {
        try {
            Denuncia denuncia = denunciaService.criarDenuncia(categoria, descricao, loginAutor, loginReportado);
            denunciaService.salvarDenunciaSimples(denuncia);
            return "redirect:/perfil/me"; // Redireciona corretamente para o perfil
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", "Erro ao criar denúncia: " + e.getMessage()); // Passa o erro para o modelo
            return "error"; // Retorna para uma página de erro, como "perfil/erro"
        }
    }
}
