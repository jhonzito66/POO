package dev.team.systers.controller;

import dev.team.systers.model.Usuario;
import dev.team.systers.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import dev.team.systers.exception.GrupoException;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Metodo que exibe a página de registro de usuário.
     *
     * @param model O modelo que é usado para passar dados entre o controlador e a visão.
     * @return O nome da página de registro.
     */
    @GetMapping("/registrar")
    public String exibirFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrar";
    }

    /**
     * Metodo que processa a requisição de registro de um novo usuário.
     * <p>
     * @param usuario O objeto Usuario que contém os dados do formulário de registro.
     * @param model O modelo que é usado para passar dados entre o controlador e a visão.
     * @return O nome da página a ser exibida após o processamento da requisição.
     *         Retorna "paginaRegistro" em caso de erro e "paginaSucesso" em caso de sucesso.
     */
    @PostMapping("/registrar_usuario")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
        /*
         * Um modelo (Model model) representa a estrutura dos dados e a lógica de negócios da aplicação.
         * Ele é usado para separar a lógica de negócios da apresentação.
         *
         * @ModelAttribute("usuario") permite que o Spring preencha automaticamente o objeto Usuario
         * a partir dos dados do formulário (no HTML).
         */
        try {
            usuarioService.registrar(usuario.getLogin().toLowerCase(), usuario.getSenha(), usuario.getEmail().toLowerCase(),
                                      usuario.getNome(), usuario.getTelefone(), usuario.getFusoHorario());
        } catch (GrupoException | IllegalArgumentException e) {

            model.addAttribute("mensagemErro", e.getMessage());
            return "registrar";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro inesperado: " + e.getMessage());
            return "registrar";
        }
        return "login";
    }

    @GetMapping("/login")
    public String exibirFormularioLogin(Model model) {
        return "login";
    }
}
