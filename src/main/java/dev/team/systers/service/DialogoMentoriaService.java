package dev.team.systers.service;

import dev.team.systers.model.DialogoMentoria;
import dev.team.systers.repository.DialogoMentoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DialogoMentoriaService {

    private final DialogoMentoriaRepository dialogoMentoriaRepository;

    @Autowired
    public DialogoMentoriaService(DialogoMentoriaRepository dialogoMentoriaRepository) {
        this.dialogoMentoriaRepository = dialogoMentoriaRepository;
    }

    // Listar todos os diálogos de mentoria
    public List<DialogoMentoria> listarTodos() {
        return dialogoMentoriaRepository.findAll();
    }

    // Buscar diálogos por participante
    public List<DialogoMentoria> listarPorParticipante(Long participanteId) {
        return dialogoMentoriaRepository.findByParticipante_Id(participanteId);
    }

    // Buscar diálogos por mentoria
    public List<DialogoMentoria> listarPorMentoria(Long mentoriaId) {
        return dialogoMentoriaRepository.findByMentoria_Id(mentoriaId);
    }

    // Buscar diálogos ocorridos após uma data
    public List<DialogoMentoria> listarPorDataHora(LocalDateTime dataHora) {
        return dialogoMentoriaRepository.findByDataHoraAfter(dataHora);
    }

    // Salvar um novo diálogo de mentoria
    public DialogoMentoria salvarDialogo(DialogoMentoria dialogoMentoria) {
        if (dialogoMentoria.getMensagem() == null || dialogoMentoria.getMensagem().isEmpty()) {
            throw new IllegalArgumentException("A mensagem do diálogo é obrigatória.");
        }
        if (dialogoMentoria.getMentoria() == null || dialogoMentoria.getParticipante() == null) {
            throw new IllegalArgumentException("A mentoria e o participante são obrigatórios.");
        }
        return dialogoMentoriaRepository.save(dialogoMentoria);
    }
}
