package dev.team.systers.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.model.Denuncia;
import dev.team.systers.repository.DenunciaRepository;

/**
 * Serviço responsável pelo gerenciamento de denúncias no sistema.
 * Fornece funcionalidades para criar, buscar, atualizar e gerenciar denúncias,
 * incluindo validações e regras de negócio específicas.
 */
@Service
public class DenunciaService {

    /**
     * Repositório para acesso aos dados de denúncias.
     */
    private final DenunciaRepository denunciaRepository;

    /**
     * Serviço para operações relacionadas a usuários.
     */
    private final UsuarioService usuarioService;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     * @param denunciaRepository Repositório de denúncias injetado pelo Spring
     * @param usuarioService Serviço de usuário injetado pelo Spring
     */
    @Autowired
    public DenunciaService(DenunciaRepository denunciaRepository, UsuarioService usuarioService) {
        this.denunciaRepository = denunciaRepository;
        this.usuarioService = usuarioService;
    }

    /**
     * Lista todas as denúncias cadastradas no sistema.
     * @return Lista de todas as denúncias com informações dos usuários
     */
    public List<Denuncia> listarTodas() {
        return denunciaRepository.findAllWithUsuarios();
    }

    /**
     * Lista denúncias por status específico.
     * @param status Status da denúncia a ser buscado
     * @return Lista de denúncias com o status especificado
     * @throws IllegalArgumentException se o status for inválido
     */
    public List<Denuncia> listarPorStatus(String status) {
        try {
            Denuncia.StatusDenuncia.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
        return denunciaRepository.findByStatus(Denuncia.StatusDenuncia.valueOf(status));
    }

    /**
     * Lista denúncias por categoria.
     * @param categoria Categoria das denúncias a serem buscadas
     * @return Lista de denúncias da categoria especificada
     * @throws IllegalArgumentException se a categoria for vazia ou nula
     */
    public List<Denuncia> listarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria não pode ser vazia");
        }
        return denunciaRepository.findByCategoria(categoria);
    }

    /**
     * Lista denúncias feitas por um usuário específico.
     * @param autorId ID do usuário autor das denúncias
     * @return Lista de denúncias feitas pelo usuário
     */
    public List<Denuncia> listarPorUsuarioAutor(Long autorId) {
        return denunciaRepository.findByUsuarioAutorIdWithUsuarios(autorId);
    }

    /**
     * Lista denúncias feitas contra um usuário específico.
     * @param usuarioId ID do usuário reportado
     * @return Lista de denúncias contra o usuário
     * @throws IllegalArgumentException se o ID do usuário for nulo
     */
    public List<Denuncia> listarPorUsuarioReportado(Long usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("ID do usuário reportado não pode ser nulo");
        }
        return denunciaRepository.findByUsuarioReportado_Id(usuarioId);
    }

    /**
     * Salva uma nova denúncia no sistema.
     * @param denuncia Denúncia a ser salva
     * @return Denúncia salva com dados atualizados
     * @throws IllegalArgumentException se a denúncia for inválida
     */
    public Denuncia salvarDenuncia(Denuncia denuncia) {
        validarDenuncia(denuncia);
        denuncia.setDataHora(LocalDateTime.now());
        denuncia.setStatus(Denuncia.StatusDenuncia.PENDENTE);
        return denunciaRepository.save(denuncia);
    }

    /**
     * Valida os dados de uma denúncia antes de salvá-la.
     * @param denuncia Denúncia a ser validada
     * @throws IllegalArgumentException se algum campo obrigatório estiver inválido
     */
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

    /**
     * Busca uma denúncia específica por ID.
     * @param id ID da denúncia
     * @return Optional contendo a denúncia se encontrada
     * @throws IllegalArgumentException se o ID for nulo
     */
    public Optional<Denuncia> buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        return denunciaRepository.findById(id);
    }

    /**
     * Atualiza uma denúncia existente.
     * @param id ID da denúncia a ser atualizada
     * @param denuncia Novos dados da denúncia
     * @return Optional contendo a denúncia atualizada se encontrada
     * @throws IllegalArgumentException se o ID ou denúncia forem nulos
     */
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

    /**
     * Lista denúncias realizadas após uma data específica.
     * @param dataHora Data/hora de referência
     * @return Lista de denúncias posteriores à data informada
     * @throws IllegalArgumentException se a data for nula
     */
    public List<Denuncia> listarPorDataHoraAfter(LocalDateTime dataHora) {
        if (dataHora == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        return denunciaRepository.findByDataHoraAfter(dataHora);
    }

    /**
     * Lista denúncias realizadas entre duas datas.
     * @param inicio Data/hora inicial
     * @param fim Data/hora final
     * @return Lista de denúncias no período especificado
     * @throws IllegalArgumentException se alguma data for nula ou inválida
     */
    public List<Denuncia> listarPorDataHoraBetween(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Datas não podem ser nulas");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data inicial não pode ser posterior à data final");
        }
        return denunciaRepository.findByDataHoraBetween(inicio, fim);
    }

    /**
     * Marca uma denúncia como resolvida.
     * @param id ID da denúncia
     * @return True se a denúncia foi resolvida com sucesso, false caso contrário
     */
    public boolean resolverDenuncia(Long id) {
        return denunciaRepository.findById(id).map(denuncia -> {
            denuncia.setStatus(Denuncia.StatusDenuncia.ATENDIDA);
            denunciaRepository.save(denuncia);
            return true;
        }).orElse(false);
    }

    /**
     * Cria uma nova denúncia com os dados básicos.
     * @param categoria Categoria da denúncia
     * @param descricao Descrição detalhada
     * @param loginAutor Login do usuário autor
     * @param loginReportado Login do usuário reportado
     * @return Nova denúncia criada
     */
    public Denuncia criarDenuncia(String categoria, String descricao, String loginAutor, String loginReportado) {
        Denuncia denuncia = new Denuncia();
        denuncia.setCategoria(categoria);
        denuncia.setDescricao(descricao);
        denuncia.setDataHora(LocalDateTime.now());
        denuncia.setUsuarioAutor(usuarioService.encontrarPorLogin(loginAutor));
        denuncia.setUsuarioReportado(usuarioService.encontrarPorLogin(loginReportado));
        return denuncia;
    }

    /**
     * Salva uma denúncia de forma simplificada.
     * @param denuncia Denúncia a ser salva
     */
    public void salvarDenunciaSimples(Denuncia denuncia) {
        denunciaRepository.save(denuncia);
    }

    /**
     * Lista todas as denúncias pendentes.
     * @return Lista de denúncias com status PENDENTE
     */
    public List<Denuncia> listarPendentes() {
        return denunciaRepository.findByStatusWithUsuarios(Denuncia.StatusDenuncia.PENDENTE);
    }
}
