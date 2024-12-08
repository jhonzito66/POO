package dev.team.systers.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.model.Denuncia;
import dev.team.systers.repository.DenunciaRepository;

@Service
public class DenunciaService {

    private final DenunciaRepository denunciaRepository;

    @Autowired
    public DenunciaService(DenunciaRepository denunciaRepository) {
        this.denunciaRepository = denunciaRepository;
    }

    // Listar todas as denúncias
    public List<Denuncia> listarTodas() {
        return denunciaRepository.findAllWithUsuarios();
    }

    // Buscar denúncias por status
    public List<Denuncia> listarPorStatus(String status) {
        // Validar se o status é válido
        try {
            Denuncia.StatusDenuncia.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
        return denunciaRepository.findByStatus(Denuncia.StatusDenuncia.valueOf(status));
    }

    // Buscar denúncias por categoria
    public List<Denuncia> listarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria não pode ser vazia");
        }
        return denunciaRepository.findByCategoria(categoria);
    }

    // Buscar denúncias feitas por um usuário específico
    public List<Denuncia> listarPorUsuarioAutor(Long usuarioId) {
        return denunciaRepository.findByUsuarioAutorIdWithUsuarios(usuarioId);
    }

    // Buscar denúncias feitas contra um usuário específico
    public List<Denuncia> listarPorUsuarioReportado(Long usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("ID do usuário reportado não pode ser nulo");
        }
        return denunciaRepository.findByUsuarioReportado_Id(usuarioId);
    }

    // Salvar uma nova denúncia
    public Denuncia salvarDenuncia(Denuncia denuncia) {
        validarDenuncia(denuncia);
        denuncia.setDataHora(LocalDateTime.now());
        denuncia.setStatus(Denuncia.StatusDenuncia.PENDENTE); // Usando o enum diretamente
        return denunciaRepository.save(denuncia);
    }

    private void validarDenuncia(Denuncia denuncia) {
        if (denuncia == null) {
            throw new IllegalArgumentException("Denúncia não pode ser nula");
        }
        if (denuncia.getDescricao() == null || denuncia.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição da denúncia é obrigatória");
        }
        if (denuncia.getCategoria() == null || denuncia.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("A categoria da denúncia é obrigatória");
        }
        if (denuncia.getUsuarioAutor() == null) {
            throw new IllegalArgumentException("O usuário autor é obrigatório");
        }
        if (denuncia.getUsuarioReportado() == null) {
            throw new IllegalArgumentException("O usuário reportado é obrigatório");
        }
        if (denuncia.getUsuarioAutor().equals(denuncia.getUsuarioReportado())) {
            throw new IllegalArgumentException("Um usuário não pode denunciar a si mesmo");
        }
    }

    // Buscar denúncia por ID
    public Optional<Denuncia> buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        return denunciaRepository.findById(id);
    }

    // Atualizar denúncia existente
    public Optional<Denuncia> atualizarDenuncia(Long id, Denuncia denuncia) {
        if (id == null || denuncia == null) {
            throw new IllegalArgumentException("ID e denúncia não podem ser nulos");
        }

        return denunciaRepository.findById(id)
            .map(denunciaExistente -> {
                denunciaExistente.setDescricao(denuncia.getDescricao());
                denunciaExistente.setCategoria(denuncia.getCategoria());
                denunciaExistente.setStatus(denuncia.getStatus());
                return denunciaRepository.save(denunciaExistente);
            });
    }

    // Listar denúncias após uma data
    public List<Denuncia> listarPorDataHoraAfter(LocalDateTime dataHora) {
        if (dataHora == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        return denunciaRepository.findByDataHoraAfter(dataHora);
    }

    // Listar denúncias entre duas datas
    public List<Denuncia> listarPorDataHoraBetween(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Datas não podem ser nulas");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data inicial não pode ser posterior à data final");
        }
        return denunciaRepository.findByDataHoraBetween(inicio, fim);
    }

}
