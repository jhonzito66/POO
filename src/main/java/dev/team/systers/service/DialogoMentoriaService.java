package dev.team.systers.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.model.DialogoMentoria;
import dev.team.systers.repository.DialogoMentoriaRepository;

/**
 * Serviço responsável pelo gerenciamento de diálogos em mentorias.
 * Fornece funcionalidades para criar, listar e gerenciar mensagens
 * trocadas durante as sessões de mentoria.
 */
@Service
public class DialogoMentoriaService {

    /**
     * Repositório para acesso aos dados de diálogos.
     */
    private final DialogoMentoriaRepository dialogoMentoriaRepository;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     * @param dialogoMentoriaRepository Repositório de diálogos injetado pelo Spring
     */
    @Autowired
    public DialogoMentoriaService(DialogoMentoriaRepository dialogoMentoriaRepository) {
        this.dialogoMentoriaRepository = dialogoMentoriaRepository;
    }

    /**
     * Lista todos os diálogos de mentoria registrados no sistema.
     * @return Lista completa de diálogos
     */
    public List<DialogoMentoria> listarTodos() {
        return dialogoMentoriaRepository.findAll();
    }

    /**
     * Lista os diálogos de um participante específico.
     * @param participanteId ID do participante
     * @return Lista de diálogos do participante
     */
    public List<DialogoMentoria> listarPorParticipante(Long participanteId) {
        return dialogoMentoriaRepository.findByParticipante_Id(participanteId);
    }

    /**
     * Lista os diálogos de uma mentoria específica.
     * @param mentoriaId ID da mentoria
     * @return Lista de diálogos da mentoria
     */
    public List<DialogoMentoria> listarPorMentoria(Long mentoriaId) {
        return dialogoMentoriaRepository.findByMentoria_Id(mentoriaId);
    }

    /**
     * Lista os diálogos ocorridos após uma data específica.
     * @param dataHora Data/hora de referência
     * @return Lista de diálogos posteriores à data informada
     */
    public List<DialogoMentoria> listarPorDataHora(LocalDateTime dataHora) {
        return dialogoMentoriaRepository.findByDataHoraAfter(dataHora);
    }

    /**
     * Salva um novo diálogo de mentoria.
     * Realiza validações básicas antes de salvar.
     * 
     * @param dialogoMentoria Diálogo a ser salvo
     * @return Diálogo salvo
     * @throws IllegalArgumentException se a mensagem estiver vazia ou mentoria/participante forem nulos
     */
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
