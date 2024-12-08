package dev.team.systers.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.team.systers.model.Denuncia;
import dev.team.systers.service.DenunciaService;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    private final DenunciaService denunciaService;

    @Autowired
    public DenunciaController(DenunciaService denunciaService) {
        this.denunciaService = denunciaService;
    }

    /**
     * Lista todas as denúncias.
     * @return Lista de denúncias.
     */
    @GetMapping
    public ResponseEntity<List<Denuncia>> listarDenuncias() {
        return ResponseEntity.ok(denunciaService.listarTodas());
    }

    /**
     * Busca uma denúncia por ID.
     * @param id ID da denúncia.
     * @return Denúncia encontrada ou erro 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Denuncia> buscarPorId(@PathVariable Long id) {
        return denunciaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Cria uma nova denúncia.
     * @param denuncia Dados da denúncia.
     * @return Denúncia criada.
     */
    @PostMapping
    public ResponseEntity<Denuncia> criarDenuncia(@RequestBody Denuncia denuncia) {
        try {
            Denuncia novaDenuncia = denunciaService.salvarDenuncia(denuncia);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaDenuncia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Atualiza uma denúncia existente.
     * @param id ID da denúncia.
     * @param denuncia Dados atualizados da denúncia.
     * @return Denúncia atualizada ou erro 404.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Denuncia> atualizarDenuncia(@PathVariable Long id, @RequestBody Denuncia denuncia) {
        return denunciaService.atualizarDenuncia(id, denuncia)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Busca denúncias por status.
     * @param status Status da denúncia.
     * @return Lista de denúncias com o status informado.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Denuncia>> buscarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(denunciaService.listarPorStatus(status));
    }

    /**
     * Busca denúncias por categoria.
     * @param categoria Categoria da denúncia.
     * @return Lista de denúncias com a categoria informada.
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Denuncia>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(denunciaService.listarPorCategoria(categoria));
    }

    /**
     * Busca denúncias feitas por um usuário específico.
     * @param usuarioId ID do autor da denúncia.
     * @return Lista de denúncias feitas pelo usuário.
     */
    @GetMapping("/autor/{usuarioId}")
    public ResponseEntity<List<Denuncia>> buscarPorUsuarioAutor(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(denunciaService.listarPorUsuarioAutor(usuarioId));
    }

    /**
     * Busca denúncias feitas contra um usuário específico.
     * @param usuarioId ID do usuário reportado.
     * @return Lista de denúncias contra o usuário.
     */
    @GetMapping("/reportado/{usuarioId}")
    public ResponseEntity<List<Denuncia>> buscarPorUsuarioReportado(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(denunciaService.listarPorUsuarioReportado(usuarioId));
    }

    /**
     * Busca denúncias feitas após uma data específica.
     * @param data Data no formato yyyy-MM-ddTHH:mm.
     * @return Lista de denúncias feitas após a data informada.
     */
    @GetMapping("/depois/{data}")
    public ResponseEntity<List<Denuncia>> buscarPorDataDepois(@PathVariable String data) {
        LocalDateTime dataHora = LocalDateTime.parse(data);
        return ResponseEntity.ok(denunciaService.listarPorDataHoraAfter(dataHora));
    }

    /**
     * Busca denúncias dentro de um intervalo de datas.
     * @param inicio Data inicial no formato yyyy-MM-ddTHH:mm.
     * @param fim Data final no formato yyyy-MM-ddTHH:mm.
     * @return Lista de denúncias no intervalo de datas.
     */
    @GetMapping("/intervalo")
    public ResponseEntity<List<Denuncia>> buscarPorIntervaloDeDatas(
            @RequestParam String inicio,
            @RequestParam String fim) {
        LocalDateTime dataInicio = LocalDateTime.parse(inicio);
        LocalDateTime dataFim = LocalDateTime.parse(fim);
        return ResponseEntity.ok(denunciaService.listarPorDataHoraBetween(dataInicio, dataFim));
    }
    @PutMapping("/resolver/{id}")
    public ResponseEntity<String> resolverDenuncia(@PathVariable Long id) {
        boolean resolvida = denunciaService.resolverDenuncia(id);
        if (resolvida) {
            return ResponseEntity.ok("Denúncia resolvida com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Denúncia não encontrada ou já resolvida.");
        }
    }

}
