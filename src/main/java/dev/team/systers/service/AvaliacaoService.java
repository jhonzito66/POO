package dev.team.systers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.team.systers.model.Avaliacao;
import dev.team.systers.model.Mentoria;
import dev.team.systers.model.Usuario;
import dev.team.systers.repository.AvaliacaoRepository;

/**
 * Serviço responsável pelo gerenciamento de avaliações de mentorias.
 * Fornece funcionalidades para criar e gerenciar avaliações feitas
 * pelos participantes após as sessões de mentoria.
 */
@Service
public class AvaliacaoService {
    
    /**
     * Repositório para acesso aos dados de avaliações.
     */
    private final AvaliacaoRepository avaliacaoRepository;

    /**
     * Construtor que inicializa o serviço com as dependências necessárias.
     * @param avaliacaoRepository Repositório de avaliações injetado pelo Spring
     */
    @Autowired
    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    /**
     * Cria uma nova avaliação para uma mentoria.
     * Registra a nota dada por um participante a uma sessão de mentoria.
     * 
     * @param mentoria Mentoria que está sendo avaliada
     * @param usuario Usuário que está realizando a avaliação
     * @param nota Nota atribuída à mentoria (0-5)
     */
    public void avaliarMentoria(Mentoria mentoria, Usuario usuario, int nota) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAvaliacaoMentoria(nota);
        avaliacao.setMentoriaAvaliada(mentoria);
        avaliacao.setParticipanteAvaliador(usuario);
        avaliacaoRepository.save(avaliacao);
    }
}
