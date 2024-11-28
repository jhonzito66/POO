package dev.team.systers.suporte;

import dev.team.systers.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DenunciaService {

    private final DenunciaRepository denunciaRepository;

    @Autowired
    public DenunciaService(DenunciaRepository denunciaRepository) {
        this.denunciaRepository = denunciaRepository;
    }

    // Listar todas as denúncias
    public List<Denuncia> listarTodas() {
        return denunciaRepository.findAll();
    }

    // Buscar denúncias por status
    public List<Denuncia> listarPorStatus(String status) {
        return denunciaRepository.findByStatus(status);
    }

    // Buscar denúncias por categoria
    public List<Denuncia> listarPorCategoria(String categoria) {
        return denunciaRepository.findByCategoria(categoria);
    }

    // Buscar denúncias feitas por um usuário específico
    public List<Denuncia> listarPorUsuarioAutor(Long usuarioId) {
        return denunciaRepository.findByUsuarioAutor_Id(usuarioId);
    }

    // Buscar denúncias feitas contra um usuário específico
    public List<Denuncia> listarPorUsuarioReportado(Long usuarioId) {
        return denunciaRepository.findByUsuarioReportado_Id(usuarioId);
    }

    // Salvar uma nova denúncia
    public Denuncia salvarDenuncia(Denuncia denuncia) {
        if (denuncia.getDescricao() == null || denuncia.getDescricao().isEmpty()) {
            throw new IllegalArgumentException("A descrição da denúncia é obrigatória.");
        }
        if (denuncia.getCategoria() == null || denuncia.getCategoria().isEmpty()) {
            throw new IllegalArgumentException("A categoria da denúncia é obrigatória.");
        }
        if (denuncia.getUsuarioAutor() == null || denuncia.getUsuarioReportado() == null) {
            throw new IllegalArgumentException("O usuário autor e o usuário reportado são obrigatórios.");
        }
        denuncia.setDataHora(LocalDateTime.now()); // A data e hora da denúncia são atribuídas automaticamente.
        return denunciaRepository.save(denuncia);
    }
}
