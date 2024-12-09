package dev.team.systers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.model.Mentoria;
import dev.team.systers.model.Participante;
import dev.team.systers.model.Usuario;
import dev.team.systers.repository.ParticipanteRepository;

/**
 * Serviço responsável pelo gerenciamento de participantes em mentorias.
 * Fornece funcionalidades para criar e buscar participantes,
 * diferenciando entre mentores e mentorados.
 */
@Service
public class ParticipanteService {
    
    /**
     * Repositório para acesso aos dados de participantes.
     */
    private final ParticipanteRepository participanteRepository;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     * @param participanteRepository Repositório de participantes injetado pelo Spring
     */
    @Autowired
    public ParticipanteService(ParticipanteRepository participanteRepository) {
        this.participanteRepository = participanteRepository;
    }

    /**
     * Cria um novo participante do tipo mentor em uma mentoria.
     * 
     * @param mentor Usuário que será o mentor
     * @param mentoria Mentoria na qual o mentor participará
     */
    public void criarParticipanteMentor(Usuario mentor, Mentoria mentoria) {
        Participante participante = new Participante();
        participante.setNome(mentor.getNome());
        participante.setUsuario(mentor);
        participante.setTipo(Participante.TipoParticipante.MENTOR);
        participante.setMentoria(mentoria);
        participanteRepository.save(participante);
    }

    /**
     * Cria um novo participante do tipo mentorado em uma mentoria.
     * 
     * @param mentorado Usuário que será o mentorado
     * @param mentoria Mentoria na qual o mentorado participará
     */
    public void criarParticipanteMentorado(Usuario mentorado, Mentoria mentoria) {
        Participante participante = new Participante();
        participante.setNome(mentorado.getNome());
        participante.setUsuario(mentorado);
        participante.setTipo(Participante.TipoParticipante.MENTORADO);
        participante.setMentoria(mentoria);
        participanteRepository.save(participante);
    }

    /**
     * Busca um participante específico em uma mentoria.
     * 
     * @param usuario Usuário participante
     * @param mentoria Mentoria em que o usuário participa
     * @return Participante encontrado ou null se não existir
     */
    public Participante buscarParticipantePorUsuarioEMentoria(Usuario usuario, Mentoria mentoria) {
        return participanteRepository.findParticipanteByUsuarioAndMentoria(usuario, mentoria);
    }
}