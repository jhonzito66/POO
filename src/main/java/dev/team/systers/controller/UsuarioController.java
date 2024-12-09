package dev.team.systers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import dev.team.systers.model.Usuario;
import dev.team.systers.service.UsuarioService;

/**
 * Controlador responsável pelo gerenciamento de usuários.
 * Gerencia operações de registro e autenticação de usuários,
 * incluindo formulários de cadastro e login.
 */
@Controller
public class UsuarioController {

    /**
     * Serviço que gerencia operações relacionadas a usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Construtor que inicializa o controlador com as dependências necessárias.
     * @param usuarioService Serviço de usuário injetado pelo Spring
     */
    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Exibe o formulário de registro de novo usuário.
     * 
     * @param model Modelo para passar dados à view
     * @return Nome da view de registro
     */
    @GetMapping("/registrar")
    public String exibirFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrar";
    }

    /**
     * Processa o registro de um novo usuário.
     * Valida os dados fornecidos e cria uma nova conta de usuário.
     * Converte login e email para minúsculas para padronização.
     * 
     * @param usuario Dados do usuário fornecidos no formulário
     * @param model Modelo para passar mensagens de erro à view
     * @return Redirecionamento para login em caso de sucesso ou volta ao formulário em caso de erro
     */
    @PostMapping("/registrar_usuario")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
        try {
            usuarioService.registrar(usuario.getLogin().toLowerCase(), 
                                   usuario.getSenha(), 
                                   usuario.getEmail().toLowerCase(),
                                   usuario.getNome(), 
                                   usuario.getTelefone(), 
                                   usuario.getFusoHorario());
            return "login";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", e.getMessage());
            return "registrar";
        }
    }

    /**
     * Exibe o formulário de login.
     * 
     * @param model Modelo para passar dados à view
     * @return Nome da view de login
     */
    @GetMapping("/login")
    public String exibirFormularioLogin(Model model) {
        return "login";
    }
}
