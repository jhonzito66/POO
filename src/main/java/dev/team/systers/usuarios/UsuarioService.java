package dev.team.systers.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.TimeZone;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean login(String login, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findAllByLogin(login);
        return usuario.isPresent() && usuario.get().getSenha().equals(senha);
    }

    public void registrar(String login, String senha, String email, String nome, String telefone, TimeZone fusoHorario) {
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setEmail(email);
        usuario.setAutorizacao(Usuario.Autorizacao.PADRAO);
        usuario.setNome(nome);
        usuario.setTelefone(telefone);
        usuario.setStatusConta(Usuario.StatusConta.NORMAL);
        usuario.setFusoHorario(fusoHorario);
        usuarioRepository.save(usuario);
    }
}
