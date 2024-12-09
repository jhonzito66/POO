package dev.team.systers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.team.systers.exception.UsuarioException;
import dev.team.systers.model.Denuncia;
import dev.team.systers.model.Perfil;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.DenunciaService;
import dev.team.systers.service.PerfilService;
import dev.team.systers.service.UsuarioService;

/**
 * Controlador responsável pelo gerenciamento de perfis de usuários.
 * Gerencia operações de visualização, edição e atualização de perfis,
 * além de funcionalidades relacionadas a mentoria e denúncias.
 */
@Controller
public class PerfilController {
    
    /**
     * Serviço que gerencia operações relacionadas a usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Serviço que gerencia operações relacionadas a perfis.
     */
    private final PerfilService perfilService;

    /**
     * Serviço que gerencia operações relacionadas a denúncias.
     */
    private final DenunciaService denunciaService;

    /**
     * Construtor que inicializa o controlador com as dependências necessárias.
     * @param perfilService Serviço de perfil injetado pelo Spring
     * @param usuarioService Serviço de usuário injetado pelo Spring
     * @param denunciaService Serviço de denúncia injetado pelo Spring
     */
    @Autowired
    public PerfilController(PerfilService perfilService, UsuarioService usuarioService, DenunciaService denunciaService) {
        this.usuarioService = usuarioService;
        this.perfilService = perfilService;
        this.denunciaService = denunciaService;
    }

    /**
     * Exibe o perfil do usuário atualmente autenticado.
     * 
     * @param model Modelo para passar dados à view
     * @return Nome da view de perfil
     * @throws UsuarioException se o usuário não for encontrado
     */
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

    /**
     * Método utilitário para adicionar o usuário autenticado ao modelo.
     * 
     * @param model Modelo para passar dados à view
     * @param usuarioService Serviço de usuário
     * @throws IllegalArgumentException se o usuário não for encontrado
     */
    static void Authentication(Model model, UsuarioService usuarioService) {
        Usuario usuario = obterUsuarioLogado(usuarioService);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        model.addAttribute("usuario", usuario);
    }

    /**
     * Exibe o perfil de um usuário específico pelo login.
     * 
     * @param login Login do usuário a ser visualizado
     * @param model Modelo para passar dados à view
     * @return Nome da view de perfil
     * @throws IllegalArgumentException se o usuário ou perfil não for encontrado
     */
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

    /**
     * Atualiza os dados do usuário e seu perfil.
     * 
     * @param usuarioUpdate Dados atualizados do usuário
     * @param perfilUpdate Dados atualizados do perfil
     * @param model Modelo para passar dados à view
     * @return Nome da view de perfil
     */
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

    /**
     * Habilita o status de mentor para o usuário atual.
     * 
     * @param model Modelo para passar dados à view
     * @return Redirecionamento para o perfil do usuário
     */
    @PostMapping("perfil/virar-mentor")
    public String virarMentor(Model model) {
        Usuario usuario = obterUsuarioLogado(usuarioService);
        usuario.setTipoMentor(true);
        usuarioService.atualizar(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("perfil", perfilService.buscarPerfilPorIDUsuario(usuario.getId()));
        return "redirect:/perfil/me";
    }

    /**
     * Método utilitário para obter o usuário atualmente autenticado.
     * 
     * @param usuarioService Serviço de usuário
     * @return Usuário autenticado
     * @throws IllegalStateException se o usuário não estiver autenticado
     */
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

    /**
     * Desabilita o status de mentor para o usuário atual.
     * 
     * @param model Modelo para passar dados à view
     * @return Redirecionamento para o perfil do usuário
     */
    @PostMapping("perfil/deixar-mentor")
    public String deixarMentor(Model model) {
        Usuario usuario = obterUsuarioLogado(usuarioService);
        usuario.setTipoMentor(false);
        usuarioService.atualizar(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("perfil", perfilService.buscarPerfilPorIDUsuario(usuario.getId()));
        return "redirect:/perfil/me";
    }

    /**
     * Exibe o formulário de edição de perfil.
     * 
     * @param usuarioId ID do usuário a ser editado
     * @param model Modelo para passar dados à view
     * @return Nome da view de edição ou erro
     */
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

    /**
     * Processa a edição do perfil.
     * 
     * @param usuarioId ID do usuário sendo editado
     * @param usuarioAtualizado Dados atualizados do usuário
     * @param model Modelo para passar dados à view
     * @return Redirecionamento para o perfil ou página de erro
     */
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

    /**
     * Cria uma nova denúncia contra um usuário.
     * 
     * @param categoria Categoria da denúncia
     * @param descricao Descrição detalhada da denúncia
     * @param loginAutor Login do usuário que está denunciando
     * @param loginReportado Login do usuário sendo denunciado
     * @param model Modelo para passar dados à view
     * @return Redirecionamento para o perfil ou página de erro
     */
    @PostMapping("perfil/denunciar")
    public String criarDenuncia(@RequestParam String categoria,
                                @RequestParam String descricao,
                                @RequestParam String loginAutor,
                                @RequestParam String loginReportado,
                                Model model) {
        try {
            Denuncia denuncia = denunciaService.criarDenuncia(categoria, descricao, loginAutor, loginReportado);
            denunciaService.salvarDenunciaSimples(denuncia);
            return "redirect:/perfil/me";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", "Erro ao criar denúncia: " + e.getMessage());
            return "error";
        }
    }
}
