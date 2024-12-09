package dev.team.systers.service;

import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.team.systers.model.Perfil;
import dev.team.systers.model.Usuario;
import dev.team.systers.repository.PerfilRepository;
import dev.team.systers.repository.UsuarioRepository;

/**
 * Serviço responsável pelo gerenciamento de usuários no sistema.
 * Fornece funcionalidades para autenticação, registro e gerenciamento de usuários,
 * incluindo operações de perfil e controle de acesso.
 */
@Service
public class UsuarioService {
    
    /**
     * Repositório para acesso aos dados de usuários.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Repositório para acesso aos dados de perfis.
     */
    private final PerfilRepository perfilRepository;

    /**
     * Codificador de senha para segurança.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     * @param usuarioRepository Repositório de usuários
     * @param perfilRepository Repositório de perfis
     * @param passwordEncoder Codificador de senha
     */
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Realiza a autenticação do usuário.
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @return true se a autenticação for bem-sucedida, false caso contrário
     */
    public boolean login(String login, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByLogin(login);
        return usuario.filter(value -> passwordEncoder.matches(senha, value.getSenha())).isPresent();
    }

    /**
     * Registra um novo usuário no sistema.
     * Cria também um perfil básico para o usuário.
     * 
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @param email Email do usuário
     * @param nome Nome completo do usuário
     * @param telefone Telefone do usuário
     * @param fusoHorario Fuso horário do usuário
     * @throws IllegalArgumentException se login ou senha forem vazios ou login já existir
     */
    public void registrar(String login, String senha, String email, String nome, String telefone, TimeZone fusoHorario) {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Login não pode ser vazio");
        }
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }
        if (usuarioRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("Login já está em uso");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setEmail(email);
        usuario.setAutorizacao(Usuario.Autorizacao.PADRAO);
        usuario.setNome(nome);
        usuario.setTelefone(telefone);
        usuario.setStatusConta(Usuario.StatusConta.NORMAL);
        usuario.setFusoHorario(fusoHorario);

        usuarioRepository.save(usuario);

        Perfil perfil = new Perfil();
        perfil.setUsuarioPerfil(usuario);
        perfil.setPerfilNome(usuario.getNome());
        perfilRepository.save(perfil);
    }

    /**
     * Busca um usuário pelo ID.
     * @param id ID do usuário
     * @return Usuário encontrado
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    public Usuario encontrarPorID(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o ID: " + id));
    }

    /**
     * Busca um usuário pelo login.
     * @param username Login do usuário
     * @return Usuário encontrado
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    public Usuario encontrarPorLogin(String username) {
        return usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o login: " + username));
    }

    /**
     * Retorna o perfil de um usuário.
     * @param usuarioId ID do usuário
     * @return Perfil do usuário
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    public Perfil visualizarPerfil(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o ID: " + usuarioId));
        return usuario.getPerfilUsuario();
    }

    /**
     * Atualiza as informações do perfil de um usuário.
     * @param usuarioId ID do usuário
     * @param nome Novo nome
     * @param email Novo email
     * @param telefone Novo telefone
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    public void editarPerfil(Long usuarioId, String nome, String email, String telefone) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o ID: " + usuarioId));
        if (nome != null && !nome.isEmpty()) usuario.setNome(nome);
        if (email != null && !email.isEmpty()) usuario.setEmail(email);
        if (telefone != null && !telefone.isEmpty()) usuario.setTelefone(telefone);
        usuarioRepository.save(usuario);
    }

    /**
     * Atualiza um usuário existente.
     * @param usuarioAtualizar Usuário com as novas informações
     * @throws IllegalArgumentException se o usuário não for encontrado
     */
    public void atualizar(Usuario usuarioAtualizar) {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioAtualizar.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + usuarioAtualizar.getId()));
        usuarioRepository.save(usuarioExistente);
    }

    /**
     * Codifica uma senha usando o algoritmo de hash configurado.
     * @param senha Senha em texto plano
     * @return Senha codificada
     */
    public String encriptarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    /**
     * Lista todos os usuários que possuem denúncias.
     * @return Lista de usuários que possuem denúncias
     */
    public List<Usuario> listarUsuariosDenunciados() {
        return usuarioRepository.findUsuariosComDenuncias();
    }

    /**
     * Atualiza o status de um usuário.
     * @param usuarioId ID do usuário a ser atualizado
     * @param novoStatus Novo status do usuário
     * @return Usuário atualizado
     * @throws IllegalArgumentException se o usuário não for encontrado ou já estiver com o status
     */
    public Usuario atualizarStatusUsuario(Long usuarioId, Usuario.StatusConta novoStatus) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        if (usuario.getStatusConta() == novoStatus) {
            throw new IllegalArgumentException("Usuário já está com este status");
        }
        
        usuario.setStatusConta(novoStatus);
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca um usuário pelo login.
     * @param name Login do usuário
     * @return Usuário encontrado ou null se não existir
     */
    public Usuario buscarPorLogin(String name) {
        return usuarioRepository.findUsuarioByLogin(name);
    }
}