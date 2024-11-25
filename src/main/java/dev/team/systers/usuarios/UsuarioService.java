package dev.team.systers.usuarios;

import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean login(String login, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByLogin(login);

        if (usuario.isEmpty()) {
            return false; // Usuario não encontrado
        }

        // Verifica a senha
        return passwordEncoder.matches(senha, usuario.get().getSenha());
    }

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

        usuarioRepository.save(usuario); // Persistencia do usuário
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o ID: " + id));
    }

    public Usuario findByLogin(String username) {
        return usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o login: " + username));
    }
}