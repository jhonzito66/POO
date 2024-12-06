package dev.team.systers.service;

import dev.team.systers.exception.MentoriaException;
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
}
