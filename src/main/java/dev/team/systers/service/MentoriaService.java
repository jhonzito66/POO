package dev.team.systers.service;

import dev.team.systers.exception.MentoriaException;
import dev.team.systers.model.Avaliacao;
import dev.team.systers.model.Mentoria;
import dev.team.systers.model.Participante;
import dev.team.systers.model.Usuario;
import dev.team.systers.repository.MentoriaRepository;
import dev.team.systers.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MentoriaService {

    private final MentoriaRepository mentoriaRepository;
    private final ParticipanteRepository participanteRepository;

    @Autowired
    public MentoriaService(MentoriaRepository mentoriaRepository, ParticipanteRepository participanteRepository) {
        this.mentoriaRepository = mentoriaRepository;
        this.participanteRepository = participanteRepository;
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
        List<Mentoria> mentorias;
        Participante.TipoParticipante tipoParticipante;
        if (usuario.getTipoMentor() != null && !usuario.getTipoMentor()) {
            tipoParticipante = Participante.TipoParticipante.MENTOR;
        } else {
            tipoParticipante = Participante.TipoParticipante.MENTORADO;
        }
        participantes = participanteRepository.findByUsuarioIdAndTipo(usuario.getId(), tipoParticipante);
        mentorias = mentoriaRepository.findMentoriasByParticipantes(participantes);
        return mentorias;
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
        if (mentoria.getDataHoraInicio() == null || mentoria.getDataHoraFim() == null) {
            throw new IllegalArgumentException("As datas de início e fim são obrigatórias.");
        }
        if (mentoria.getDataHoraInicio().isAfter(mentoria.getDataHoraFim())) {
            throw new IllegalArgumentException("A data de início deve ser anterior à data de fim.");
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
}
