package dev.team.systers.service;

import dev.team.systers.model.Avaliacao;
import dev.team.systers.model.Mentoria;
import dev.team.systers.model.Usuario;
import dev.team.systers.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoService {
    private final AvaliacaoRepository avaliacaoRepository;

    @Autowired
    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    /**
     * Criar avaliação.
     */
    public void avaliarMentoria(Mentoria mentoria, Usuario usuario, int nota) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAvaliacaoMentoria(nota);
        avaliacao.setMentoriaAvaliada(mentoria);
        avaliacao.setParticipanteAvaliador(usuario);
        avaliacaoRepository.save(avaliacao);
    }
}
