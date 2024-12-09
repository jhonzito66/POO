package dev.team.systers.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.team.systers.exception.GrupoException;
import dev.team.systers.exception.UsuarioException;
import dev.team.systers.model.Grupo;
import dev.team.systers.model.Membro;
import dev.team.systers.model.Postagem;
import dev.team.systers.model.Usuario;
import dev.team.systers.service.ComentarioService;
import dev.team.systers.service.GrupoService;
import dev.team.systers.service.MembroService;
import dev.team.systers.service.PostagemService;
import dev.team.systers.service.UsuarioService;

/**
 * Controlador responsável pelo gerenciamento de grupos.
 * Gerencia operações de criação, visualização e interação em grupos,
 * incluindo postagens e comentários dos membros.
 */
@Controller
public class GrupoController {
    
    /**
     * Serviço que gerencia operações relacionadas a grupos.
     */
    private final GrupoService grupoService;

    /**
     * Serviço que gerencia operações relacionadas a usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Serviço que gerencia operações relacionadas a postagens.
     */
    private final PostagemService postagemService;

    /**
     * Serviço que gerencia operações relacionadas a comentários.
     */
    private final ComentarioService comentarioService;

    /**
     * Serviço que gerencia operações relacionadas a membros.
     */
    private final MembroService membroService;

    /**
     * Construtor que inicializa o controlador com as dependências necessárias.
     * @param grupoService Serviço de grupo injetado pelo Spring
     * @param usuarioService Serviço de usuário injetado pelo Spring
     * @param postagemService Serviço de postagem injetado pelo Spring
     * @param comentarioService Serviço de comentário injetado pelo Spring
     * @param membroService Serviço de membro injetado pelo Spring
     */
    @Autowired
    public GrupoController(GrupoService grupoService, UsuarioService usuarioService, PostagemService postagemService, ComentarioService comentarioService, MembroService membroService) {
        this.grupoService = grupoService;
        this.usuarioService = usuarioService;
        this.postagemService = postagemService;
        this.comentarioService = comentarioService;
        this.membroService = membroService;
    }

    /**
     * Exibe a lista de grupos do usuário autenticado.
     * 
     * @param model Modelo para passar dados à view
     * @return Nome da view de grupos
     * @throws UsuarioException se o usuário não estiver autenticado ou não for encontrado
     */
    @GetMapping("/grupos")
    public String exibirGrupos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("title", "grupos");
        model.addAttribute("content", "grupos");
        model.addAttribute("sidebar", "perfil-template");

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getName().equals("anonymousUser")) {
            throw new UsuarioException("Usuário não autenticado");
        }

        String username = auth.getName();
        Usuario usuario = usuarioService.encontrarPorLogin(username);
        if (usuario == null) {
            throw new UsuarioException("Usuário não encontrado");
        }
        model.addAttribute("usuario", usuario);

        List<Grupo> grupos = grupoService.listarGruposPorUsuario(usuario);
        model.addAttribute("grupos", grupos);

        return "grupos";
    }

    /**
     * Exibe o formulário de criação de grupo.
     * 
     * @param model Modelo para passar dados à view
     * @return Nome da view de criação de grupo
     */
    @GetMapping("/grupos/criar-grupo")
    public String registrarGrupo(Model model) {
        model.addAttribute("grupo", new Grupo());
        return "/criar-grupo";
    }

    /**
     * Processa a criação de um novo grupo.
     * 
     * @param grupo Dados do grupo a ser criado
     * @param model Modelo para passar dados à view
     * @return Redirecionamento para a lista de grupos ou página de erro
     * @throws UsuarioException se o usuário não estiver autenticado ou não for encontrado
     */
    @PostMapping("/grupos/criar-grupo")
    public String registrarGrupo(@ModelAttribute("grupo") Grupo grupo, Model model) {
        model.addAttribute("grupo", new Grupo());

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null ||
                    !auth.isAuthenticated() ||
                    auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            String username = auth.getName();
            Usuario usuario = usuarioService.encontrarPorLogin(username);

            if (usuario == null) {
                throw new UsuarioException("Usuário não encontrado");
            }
            model.addAttribute("usuario", usuario);

            grupoService.criarGrupo(grupo.getNome(), grupo.getDescricao(), usuario);
        } catch (GrupoException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            return "/grupos/criar-grupo";
        }
        return "redirect:/grupos";
    }

    /**
     * Exibe os detalhes de um grupo específico.
     * Inclui postagens e informações de membros se o usuário for membro do grupo.
     * 
     * @param id ID do grupo a ser visualizado
     * @param model Modelo para passar dados à view
     * @return Nome da view do grupo ou redirecionamento em caso de erro
     */
    @GetMapping("/grupos/grupo/{id}")
    public String visualizarGrupo(@PathVariable Long id, Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuario = usuarioService.buscarPorLogin(auth.getName());
            Grupo grupo = grupoService.buscarGrupoPorId(id);
            
            boolean isMembro = grupo.getMembros().stream()
                    .anyMatch(m -> m.getUsuario().getId().equals(usuario.getId()));
            
            model.addAttribute("grupo", grupo);
            model.addAttribute("isMembro", isMembro);
            
            if (isMembro) {
                Membro membroAtual = membroService.buscarMembroPorUsuarioEGrupo(usuario.getId(), grupo.getId());
                List<Postagem> postagens = grupoService.visualizarPostagens(id);
                
                model.addAttribute("membro", membroAtual);
                model.addAttribute("postagens", postagens);
            }
            
            return "grupo";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", e.getMessage());
            return "redirect:/grupos";
        }
    }

    /**
     * Exclui um grupo específico.
     * Apenas o criador do grupo pode excluí-lo.
     * 
     * @param id ID do grupo a ser excluído
     * @param redirectAttributes Atributos para mensagens de feedback
     * @return Redirecionamento para a lista de grupos
     */
    @PostMapping("/grupos/excluir/{id}")
    public String excluirGrupo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            String username = auth.getName();
            Usuario usuarioAutenticado = usuarioService.encontrarPorLogin(username);
            if (usuarioAutenticado == null) {
                throw new UsuarioException("Usuário não encontrado");
            }

            grupoService.excluirGrupo(id, usuarioAutenticado);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Grupo excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir grupo: " + e.getMessage());
        }

        return "redirect:/grupos";
    }

    /**
     * Cria uma nova postagem em um grupo.
     * 
     * @param id ID do grupo onde a postagem será criada
     * @param conteudo Texto da postagem
     * @param redirectAttributes Atributos para mensagens de feedback
     * @return Redirecionamento para a página do grupo
     */
    @PostMapping("/grupos/grupo/{id}/postagem")
    public String criarPostagem(@PathVariable Long id, 
                              @RequestParam String conteudo, 
                              RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            Usuario usuario = usuarioService.encontrarPorLogin(auth.getName());
            if (usuario == null) {
                throw new UsuarioException("Usuário não encontrado");
            }

            postagemService.criarPostagem(id, conteudo, usuario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Postagem criada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao criar postagem: " + e.getMessage());
        }
        return "redirect:/grupos/grupo/" + id;
    }

    /**
     * Exclui uma postagem específica.
     * Apenas o autor da postagem pode excluí-la.
     * 
     * @param grupoId ID do grupo da postagem
     * @param postagemId ID da postagem a ser excluída
     * @param redirectAttributes Atributos para mensagens de feedback
     * @return Redirecionamento para a página do grupo
     */
    @PostMapping("/grupos/grupo/{grupoId}/postagem/{postagemId}/excluir")
    public String excluirPostagem(@PathVariable Long grupoId,
                                @PathVariable Long postagemId,
                                RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            Usuario usuario = usuarioService.encontrarPorLogin(auth.getName());
            if (usuario == null) {
                throw new UsuarioException("Usuário não encontrado");
            }

            postagemService.excluirPostagem(postagemId, usuario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Postagem excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir postagem: " + e.getMessage());
        }
        return "redirect:/grupos/grupo/" + grupoId;
    }

    /**
     * Cria um novo comentário em uma postagem.
     * 
     * @param grupoId ID do grupo da postagem
     * @param postagemId ID da postagem a ser comentada
     * @param conteudo Texto do comentário
     * @param redirectAttributes Atributos para mensagens de feedback
     * @return Redirecionamento para a página do grupo
     */
    @PostMapping("/grupos/grupo/{grupoId}/postagem/{postagemId}/comentario")
    public String criarComentario(@PathVariable Long grupoId,
                                @PathVariable Long postagemId,
                                @RequestParam String conteudo,
                                RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            Usuario usuario = usuarioService.encontrarPorLogin(auth.getName());
            if (usuario == null) {
                throw new UsuarioException("Usuário não encontrado");
            }

            comentarioService.criarComentario(postagemId, conteudo, usuario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Comentário adicionado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao adicionar comentário: " + e.getMessage());
        }
        return "redirect:/grupos/grupo/" + grupoId;
    }

    /**
     * Exclui um comentário específico.
     * Apenas o autor do comentário pode excluí-lo.
     * 
     * @param grupoId ID do grupo do comentário
     * @param comentarioId ID do comentário a ser excluído
     * @param redirectAttributes Atributos para mensagens de feedback
     * @return Redirecionamento para a página do grupo
     */
    @PostMapping("/grupos/grupo/{grupoId}/comentario/{comentarioId}/excluir")
    public String excluirComentario(@PathVariable Long grupoId,
                                  @PathVariable Long comentarioId,
                                  RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            Usuario usuario = usuarioService.encontrarPorLogin(auth.getName());
            if (usuario == null) {
                throw new UsuarioException("Usuário não encontrado");
            }

            comentarioService.excluirComentario(comentarioId, usuario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Comentário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir comentário: " + e.getMessage());
        }
        return "redirect:/grupos/grupo/" + grupoId;
    }

    /**
     * Permite que um usuário participe de um grupo específico.
     * 
     * @param id ID do grupo que o usuário deseja participar
     * @param redirectAttributes Atributos para mensagens de feedback
     * @return Redirecionamento para a página do grupo ou lista de grupos em caso de erro
     * @throws UsuarioException se o usuário não estiver autenticado
     */
    @PostMapping("/grupos/participar/{id}")
    public String participarGrupo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            Usuario usuario = usuarioService.buscarPorLogin(auth.getName());
            grupoService.participarGrupo(id, usuario.getId());

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Você agora é membro do grupo!");
            return "redirect:/grupos/grupo/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao participar do grupo: " + e.getMessage());
            return "redirect:/grupos";
        }
    }

    /**
     * Permite que um usuário saia de um grupo específico.
     * Apenas membros que não são donos podem sair do grupo.
     * 
     * @param id ID do grupo que o usuário deseja sair
     * @param redirectAttributes Atributos para mensagens de feedback
     * @return Redirecionamento para a lista de grupos
     */
    @PostMapping("/grupos/sair/{id}")
    public String sairGrupo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
                throw new UsuarioException("Usuário não autenticado");
            }

            Usuario usuario = usuarioService.encontrarPorLogin(auth.getName());
            if (usuario == null) {
                throw new UsuarioException("Usuário não encontrado");
            }

            grupoService.deixarGrupo(id, usuario.getId());
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Você saiu do grupo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao sair do grupo: " + e.getMessage());
        }
        return "redirect:/grupos";
    }

    /**
     * Pesquisa grupos com base em um termo de busca.
     * Se nenhum termo for fornecido, lista todos os grupos ordenados por número de membros.
     * Exibe apenas grupos dos quais o usuário ainda não participa.
     * 
     * @param q Termo de busca opcional para filtrar grupos por nome
     * @param model Modelo para passar dados à view
     * @return Nome da view de pesquisa de grupos
     */
    @GetMapping("/grupos/pesquisar")
    public String pesquisarGrupos(@RequestParam(required = false) String q, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = usuarioService.buscarPorLogin(auth.getName());
        List<Grupo> todosGrupos;

        if (q != null && !q.trim().isEmpty()) {
            todosGrupos = grupoService.buscarGruposPorNome(q);
        } else {
            todosGrupos = grupoService.listarTodosGruposOrdenadosPorMembros();
        }

        List<Grupo> gruposDoUsuario = grupoService.listarGruposPorUsuario(usuarioLogado);

        List<Grupo> gruposNaoParticipa = todosGrupos.stream()
            .filter(grupo -> !gruposDoUsuario.contains(grupo))
            .collect(Collectors.toList());

        model.addAttribute("grupos", gruposNaoParticipa);
        model.addAttribute("usuario", usuarioLogado);
        model.addAttribute("title", "Pesquisar Grupos");
        model.addAttribute("content", "pesquisar-grupos");
        return "pesquisar-grupos";
    }
}
