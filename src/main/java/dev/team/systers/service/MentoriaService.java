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

@Service
public class MentoriaService {

    private final MentoriaRepository mentoriaRepository;
    private final ParticipanteRepository participanteRepository;
    private final AvaliacaoRepository avaliacaoRepository;

    @Autowired
    public MentoriaService(MentoriaRepository mentoriaRepository, ParticipanteRepository participanteRepository, AvaliacaoRepository avaliacaoRepository) {
        this.mentoriaRepository = mentoriaRepository;
        this.participanteRepository = participanteRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    // Listar todas as mentorias
    public List<Mentoria> listarTodas() {
        return mentoriaRepository.findAll();
    }

    // Buscar mentoria por status
    public List<Mentoria> listarPorStatus(String status) {
        return mentoriaRepository.findByStatus(status);
    }

    // Buscar mentorias por nome
    public List<Mentoria> listarPorNome(String nome) {
        return mentoriaRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Buscar mentorias por data de início após uma data
    public List<Mentoria> listarPorDataInicio(LocalDateTime dataHoraInicio) {
        return mentoriaRepository.findByDataHoraInicioAfter(dataHoraInicio);
    }

    public Mentoria salvarMentoria(Mentoria mentoria) {
        if (mentoria.getNome() == null || mentoria.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome da mentoria é obrigatório.");
        }
        if (mentoria.getDataHoraInicio() == null || mentoria.getDataHoraFim() == null) {
            throw new IllegalArgumentException("As datas de início e fim são obrigatórias.");
        }
        return mentoriaRepository.save(mentoria);
    }

    public List<Mentoria> listarMentoriasPorUsuario(Usuario usuario) {
        List<Participante> participantes;
        // Se o usuário for mentor, busca mentorias onde ele é mentor
        if (usuario.getTipoMentor() != null && usuario.getTipoMentor()) {
            participantes = participanteRepository.findByUsuarioIdAndTipo(
                usuario.getId(), 
                Participante.TipoParticipante.MENTOR
            );
        } 
        // Se não for mentor, busca mentorias onde ele é mentorado
        else {
            participantes = participanteRepository.findByUsuarioIdAndTipo(
                usuario.getId(), 
                Participante.TipoParticipante.MENTORADO
            );
        }

        // Extrair as mentorias dos participantes
        return participantes.stream()
            .map(Participante::getMentoria)
            .toList();
    }

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

        // Definir o status inicial da mentoria
        mentoria.setStatus("Agendada");
        return mentoriaRepository.save(mentoria);
    }

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

    public void avaliarMentoria(Avaliacao avaliacao) {
        Mentoria mentoria = avaliacao.getMentoriaAvaliada();
        
        // Verificar se a mentoria está finalizada
        if (!mentoria.getStatus().equals("Concluída")) {
            throw new MentoriaException("Só é possível avaliar mentorias finalizadas");
        }
        
        // Verificar se o usuário é participante da mentoria como mentorado
        boolean isParticipante = mentoria.getParticipantes().stream()
            .anyMatch(p -> p.getUsuario().getId().equals(avaliacao.getParticipanteAvaliador().getId()) 
                    && p.getTipo() == Participante.TipoParticipante.MENTORADO);
                    
        if (!isParticipante) {
            throw new MentoriaException("Apenas o mentorado participante pode avaliar a mentoria");
        }
        
        // Verificar se já existe avaliação deste usuário para esta mentoria
        if (avaliacaoRepository.existsByMentoriaAvaliadaAndParticipanteAvaliador(
                mentoria, avaliacao.getParticipanteAvaliador())) {
            throw new MentoriaException("Mentoria já foi avaliada por este usuário");
        }
        
        avaliacaoRepository.save(avaliacao);
    }

    public Mentoria buscarPorId(Long mentoriaId) {
        return mentoriaRepository.findMentoriaById(mentoriaId);
    }

    public boolean jaFoiAvaliada(Long mentoriaId, Long usuarioId) {
        return avaliacaoRepository.existsByMentoriaAvaliadaIdAndParticipanteAvaliadorId(mentoriaId, usuarioId);
    }

    public Avaliacao buscarAvaliacao(Long mentoriaId) {
        return avaliacaoRepository.findByMentoriaAvaliadaId(mentoriaId);
    }
}
