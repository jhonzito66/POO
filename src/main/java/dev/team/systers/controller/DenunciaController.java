package dev.team.systers.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.team.systers.model.Denuncia;
import dev.team.systers.service.DenunciaService;

/**
 * Controlador REST para operações relacionadas a denúncias.
 * Fornece endpoints para gerenciamento completo de denúncias,
 * incluindo listagem, busca, atualização e resolução.
 */
@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    /**
     * Serviço que gerencia operações relacionadas a denúncias.
     */
    private final DenunciaService denunciaService;

    /**
     * Construtor que inicializa o controlador com as dependências necessárias.
     * @param denunciaService Serviço de denúncia injetado pelo Spring
     */
    @Autowired
    public DenunciaController(DenunciaService denunciaService) {
        this.denunciaService = denunciaService;
    }

    /**
     * Lista todas as denúncias registradas no sistema.
     * 
     * @return ResponseEntity contendo a lista de todas as denúncias
     */
    @GetMapping
    public ResponseEntity<List<Denuncia>> listarDenuncias() {
        return ResponseEntity.ok(denunciaService.listarTodas());
    }

    /**
     * Busca uma denúncia específica pelo seu ID.
     * 
     * @param id ID da denúncia a ser buscada
     * @return ResponseEntity contendo a denúncia ou NOT_FOUND se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<Denuncia> buscarPorId(@PathVariable Long id) {
        return denunciaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Atualiza os dados de uma denúncia existente.
     * 
     * @param id ID da denúncia a ser atualizada
     * @param denuncia Novos dados da denúncia
     * @return ResponseEntity contendo a denúncia atualizada ou NOT_FOUND se não existir
     */
    @PutMapping("/{id}")
    public ResponseEntity<Denuncia> atualizarDenuncia(@PathVariable Long id, @RequestBody Denuncia denuncia) {
        return denunciaService.atualizarDenuncia(id, denuncia)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Busca denúncias por seu status atual.
     * 
     * @param status Status da denúncia (ex: PENDENTE, RESOLVIDA)
     * @return ResponseEntity contendo a lista de denúncias com o status especificado
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Denuncia>> buscarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(denunciaService.listarPorStatus(status));
    }

    /**
     * Busca denúncias por categoria.
     * 
     * @param categoria Categoria da denúncia
     * @return ResponseEntity contendo a lista de denúncias da categoria especificada
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Denuncia>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(denunciaService.listarPorCategoria(categoria));
    }

    /**
     * Busca denúncias feitas por um usuário específico.
     * 
     * @param usuarioId ID do usuário autor das denúncias
     * @return ResponseEntity contendo a lista de denúncias feitas pelo usuário
     */
    @GetMapping("/autor/{usuarioId}")
    public ResponseEntity<List<Denuncia>> buscarPorUsuarioAutor(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(denunciaService.listarPorUsuarioAutor(usuarioId));
    }

    /**
     * Busca denúncias feitas contra um usuário específico.
     * 
     * @param usuarioId ID do usuário que foi denunciado
     * @return ResponseEntity contendo a lista de denúncias contra o usuário
     */
    @GetMapping("/reportado/{usuarioId}")
    public ResponseEntity<List<Denuncia>> buscarPorUsuarioReportado(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(denunciaService.listarPorUsuarioReportado(usuarioId));
    }

    /**
     * Busca denúncias feitas após uma data específica.
     * 
     * @param data Data de referência no formato yyyy-MM-ddTHH:mm
     * @return ResponseEntity contendo a lista de denúncias posteriores à data
     */
    @GetMapping("/depois/{data}")
    public ResponseEntity<List<Denuncia>> buscarPorDataDepois(@PathVariable String data) {
        LocalDateTime dataHora = LocalDateTime.parse(data);
        return ResponseEntity.ok(denunciaService.listarPorDataHoraAfter(dataHora));
    }

    /**
     * Busca denúncias dentro de um intervalo de datas.
     * 
     * @param inicio Data inicial no formato yyyy-MM-ddTHH:mm
     * @param fim Data final no formato yyyy-MM-ddTHH:mm
     * @return ResponseEntity contendo a lista de denúncias no período
     */
    @GetMapping("/intervalo")
    public ResponseEntity<List<Denuncia>> buscarPorIntervaloDeDatas(
            @RequestParam String inicio,
            @RequestParam String fim) {
        LocalDateTime dataInicio = LocalDateTime.parse(inicio);
        LocalDateTime dataFim = LocalDateTime.parse(fim);
        return ResponseEntity.ok(denunciaService.listarPorDataHoraBetween(dataInicio, dataFim));
    }

    /**
     * Marca uma denúncia como resolvida.
     * Endpoint restrito a administradores.
     * 
     * @param id ID da denúncia a ser resolvida
     * @param authentication Informações do usuário autenticado
     * @return ResponseEntity com mensagem de sucesso ou erro
     */
    @PutMapping("/resolver/{id}")
    public ResponseEntity<String> resolverDenuncia(@PathVariable Long id, Authentication authentication) {
        System.out.println("Tentativa de resolver denúncia " + id);
        System.out.println("Usuário autenticado: " + authentication.getName());
        System.out.println("Roles do usuário: " + authentication.getAuthorities());
        
        boolean resolvida = denunciaService.resolverDenuncia(id);
        if (resolvida) {
            return ResponseEntity.ok("Denúncia resolvida com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Denúncia não encontrada ou já resolvida.");
        }
    }
}
