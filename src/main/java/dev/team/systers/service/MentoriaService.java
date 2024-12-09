package dev.team.systers.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.exception.MentoriaException;
import dev.team.systers.model.Avaliacao;
import dev.team.systers.model.Mentoria;
import dev.team.systers.model.Participante;
import dev.team.systers.model.Usuario;
import dev.team.systers.repository.AvaliacaoRepository;
import dev.team.systers.repository.MentoriaRepository;
import dev.team.systers.repository.ParticipanteRepository;

/**
 * Serviço responsável pelo gerenciamento de mentorias no sistema.
 * Fornece funcionalidades para criar, gerenciar e avaliar mentorias,
 * incluindo agendamento, participação e avaliações.
 */
@Service
public class MentoriaService {

    /**
     * Repositório para acesso aos dados de mentorias.
     */
    private final MentoriaRepository mentoriaRepository;

    /**
     * Repositório para acesso aos dados de participantes.
     */
    private final ParticipanteRepository participanteRepository;

    /**
     * Repositório para acesso aos dados de avaliações.
     */
    private final AvaliacaoRepository avaliacaoRepository;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     * @param mentoriaRepository Repositório de mentorias
     * @param participanteRepository Repositório de participantes
     * @param avaliacaoRepository Repositório de avaliações
     */
    @Autowired
    public MentoriaService(MentoriaRepository mentoriaRepository, ParticipanteRepository participanteRepository, AvaliacaoRepository avaliacaoRepository) {
        this.mentoriaRepository = mentoriaRepository;
        this.participanteRepository = participanteRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    /**
     * Lista todas as mentorias cadastradas no sistema.
     * @return Lista completa de mentorias
     */
    public List<Mentoria> listarTodas() {
        return mentoriaRepository.findAll();
    }

    /**
     * Lista mentorias por status específico.
     * @param status Status da mentoria (ex: Agendada, Em Andamento, Concluída)
     * @return Lista de mentorias com o status especificado
     */
    public List<Mentoria> listarPorStatus(String status) {
        return mentoriaRepository.findByStatus(status);
    }

    /**
     * Lista mentorias que contenham o texto no nome.
     * @param nome Texto a ser buscado no nome da mentoria
     * @return Lista de mentorias que correspondem à busca
     */
    public List<Mentoria> listarPorNome(String nome) {
        return mentoriaRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Lista mentorias que começam após uma data específica.
     * @param dataHoraInicio Data/hora de referência
     * @return Lista de mentorias futuras à data especificada
     */
    public List<Mentoria> listarPorDataInicio(LocalDateTime dataHoraInicio) {
        return mentoriaRepository.findByDataHoraInicioAfter(dataHoraInicio);
    }

    /**
     * Salva uma nova mentoria no sistema.
     * @param mentoria Mentoria a ser salva
     * @return Mentoria salva
     * @throws IllegalArgumentException se dados obrigatórios estiverem faltando
     */
    public Mentoria salvarMentoria(Mentoria mentoria) {
        if (mentoria.getNome() == null || mentoria.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome da mentoria é obrigatório.");
        }
        if (mentoria.getDataHoraInicio() == null || mentoria.getDataHoraFim() == null) {
            throw new IllegalArgumentException("As datas de início e fim são obrigatórias.");
        }
        return mentoriaRepository.save(mentoria);
    }

    /**
     * Lista mentorias associadas a um usuário.
     * Se o usuário for mentor, lista mentorias onde ele é mentor.
     * Se não for mentor, lista mentorias onde ele é mentorado.
     * 
     * @param usuario Usuário para buscar as mentorias
     * @return Lista de mentorias do usuário
     */
    public List<Mentoria> listarMentoriasPorUsuario(Usuario usuario) {
        List<Participante> participantes;
        if (usuario.getTipoMentor() != null && usuario.getTipoMentor()) {
            participantes = participanteRepository.findByUsuarioIdAndTipo(
                usuario.getId(), 
                Participante.TipoParticipante.MENTOR
            );
        } else {
            participantes = participanteRepository.findByUsuarioIdAndTipo(
                usuario.getId(), 
                Participante.TipoParticipante.MENTORADO
            );
        }

        return participantes.stream()
            .map(Participante::getMentoria)
            .toList();
    }

    /**
     * Permite que um mentorado solicite participação em uma mentoria.
     * @param mentoriaId ID da mentoria
     * @param mentee Usuário solicitante (mentorado)
     * @return Mentoria atualizada
     * @throws MentoriaException se a mentoria não existir ou não estiver disponível
     */
    public Mentoria solicitarMentoria(Long mentoriaId, Usuario mentee) {
        Mentoria mentoria = mentoriaRepository.findById(mentoriaId)
                .orElseThrow(() -> new MentoriaException("Mentoria não encontrada"));

        if (mentoria.getStatus().equals("Concluída") || mentoria.getStatus().equals("Cancelada")) {
            throw new MentoriaException("Mentoria não disponível para solicitação.");
        }

        Participante participante = new Participante();
        participante.setMentoria(mentoria);
        participante.setUsuario(mentee);
        participante.setTipo(Participante.TipoParticipante.MENTORADO);
        participanteRepository.save(participante);

        if (mentoria.getStatus().equals("Agendada")) {
            mentoria.setStatus("Em Andamento");
            mentoriaRepository.save(mentoria);
        }

        return mentoria;
    }

    /**
     * Permite que um mentor ofereça uma nova mentoria.
     * @param mentoria Dados da mentoria a ser oferecida
     * @param mentor Usuário que está oferecendo a mentoria
     * @return Mentoria criada
     * @throws MentoriaException se o usuário não for mentor
     * @throws IllegalArgumentException se dados obrigatórios estiverem faltando
     */
    public Mentoria oferecerMentoria(Mentoria mentoria, Usuario mentor) {
        if (mentor.getTipoMentor() == null || !mentor.getTipoMentor()) {
            throw new MentoriaException("Usuário não é um mentor.");
        }

        if (mentoria.getNome() == null || mentoria.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome da mentoria é obrigatório.");
        }
        if (mentoria.getDataHoraInicio() == null) {
            throw new IllegalArgumentException("A data de início é obrigatória.");
        }
        if (mentoria.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data de início deve ser futura.");
        }

        mentoria.setStatus("Agendada");
        return mentoriaRepository.save(mentoria);
    }

    /**
     * Atualiza informações de uma mentoria existente.
     * @param mentoriaId ID da mentoria
     * @param mentoriaAtualizada Novos dados da mentoria
     * @return Mentoria atualizada
     * @throws MentoriaException se a mentoria não existir
     */
    public Mentoria gerenciarAgenda(Long mentoriaId, Mentoria mentoriaAtualizada) {
        Mentoria mentoriaExistente = mentoriaRepository.findById(mentoriaId)
                .orElseThrow(() -> new MentoriaException("Mentoria não encontrada"));

        if (mentoriaAtualizada.getNome() != null) {
            mentoriaExistente.setNome(mentoriaAtualizada.getNome());
        }
        if (mentoriaAtualizada.getDataHoraInicio() != null) {
            mentoriaExistente.setDataHoraInicio(mentoriaAtualizada.getDataHoraInicio());
        }
        if (mentoriaAtualizada.getDataHoraFim() != null) {
            mentoriaExistente.setDataHoraFim(mentoriaAtualizada.getDataHoraFim());
        }
        if (mentoriaAtualizada.getStatus() != null) {
            mentoriaExistente.setStatus(mentoriaAtualizada.getStatus());
        }

        return mentoriaRepository.save(mentoriaExistente);
    }

    /**
     * Finaliza uma mentoria em andamento.
     * @param mentoriaId ID da mentoria
     * @return Mentoria finalizada
     * @throws MentoriaException se a mentoria não existir ou já estiver finalizada
     */
    public Mentoria finalizarMentoria(Long mentoriaId) {
        Mentoria mentoria = mentoriaRepository.findById(mentoriaId)
                .orElseThrow(() -> new MentoriaException("Mentoria não encontrada"));

        if (mentoria.getStatus().equals("Concluída")) {
            throw new MentoriaException("Mentoria já está finalizada.");
        }

        mentoria.setStatus("Concluída");
        mentoria.setDataHoraFim(LocalDateTime.now());
        
        return mentoriaRepository.save(mentoria);
    }

    /**
     * Registra uma avaliação para uma mentoria concluída.
     * Apenas o mentorado pode avaliar a mentoria.
     * 
     * @param avaliacao Avaliação a ser registrada
     * @throws MentoriaException se a mentoria não estiver concluída, usuário não for participante ou já tiver avaliado
     */
    public void avaliarMentoria(Avaliacao avaliacao) {
        Mentoria mentoria = avaliacao.getMentoriaAvaliada();
        
        if (!mentoria.getStatus().equals("Concluída")) {
            throw new MentoriaException("Só é possível avaliar mentorias finalizadas");
        }
        
        boolean isParticipante = mentoria.getParticipantes().stream()
            .anyMatch(p -> p.getUsuario().getId().equals(avaliacao.getParticipanteAvaliador().getId()) 
                    && p.getTipo() == Participante.TipoParticipante.MENTORADO);
                    
        if (!isParticipante) {
            throw new MentoriaException("Apenas o mentorado participante pode avaliar a mentoria");
        }
        
        if (avaliacaoRepository.existsByMentoriaAvaliadaAndParticipanteAvaliador(
                mentoria, avaliacao.getParticipanteAvaliador())) {
            throw new MentoriaException("Mentoria já foi avaliada por este usuário");
        }
        
        avaliacaoRepository.save(avaliacao);
    }

    /**
     * Busca uma mentoria pelo ID.
     * @param mentoriaId ID da mentoria
     * @return Mentoria encontrada ou null se não existir
     */
    public Mentoria buscarPorId(Long mentoriaId) {
        return mentoriaRepository.findMentoriaById(mentoriaId);
    }

    /**
     * Verifica se uma mentoria já foi avaliada por um usuário.
     * @param mentoriaId ID da mentoria
     * @param usuarioId ID do usuário
     * @return true se já existe avaliação, false caso contrário
     */
    public boolean jaFoiAvaliada(Long mentoriaId, Long usuarioId) {
        return avaliacaoRepository.existsByMentoriaAvaliadaIdAndParticipanteAvaliadorId(mentoriaId, usuarioId);
    }

    /**
     * Busca a avaliação de uma mentoria.
     * @param mentoriaId ID da mentoria
     * @return Avaliação encontrada ou null se não existir
     */
    public Avaliacao buscarAvaliacao(Long mentoriaId) {
        return avaliacaoRepository.findByMentoriaAvaliadaId(mentoriaId);
    }
}
