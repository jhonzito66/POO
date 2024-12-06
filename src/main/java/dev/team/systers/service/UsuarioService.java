package dev.team.systers.service;

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

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
      
        this.passwordEncoder = passwordEncoder;
    }

    public boolean login(String login, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByLogin(login);

        // Todo: Usuario não encontrado
        // Todo: Senha inválida

        return usuario.filter(value -> passwordEncoder.matches(senha, value.getSenha())).isPresent();
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

        usuarioRepository.save(usuario); // Persistência do usuário primeiro; depois o perfil!

        Perfil perfil = new Perfil();
        perfil.setUsuarioPerfil(usuario);
        perfil.setPerfilNome(usuario.getNome());
        perfilRepository.save(perfil);
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