package dsitp.backend.project.rest;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.Periodo;
import dsitp.backend.project.model.ReservaEsporadicaDTO;
import dsitp.backend.project.model.ReservaRespuestaDTO;
import dsitp.backend.project.repos.BedelRepository;
import dsitp.backend.project.repos.PeriodoRepository;
import dsitp.backend.project.service.ReservaEsporadicaService;
import dsitp.backend.project.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/reservasEsporadicas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservaEsporadicaResource {

    private final ReservaEsporadicaService reservaEsporadicaService;
    private final PeriodoRepository periodoRepository;
    private final BedelRepository bedelRepository;

    public ReservaEsporadicaResource(final ReservaEsporadicaService reservaEsporadicaService,
            final PeriodoRepository periodoRepository, final BedelRepository bedelRepository) {
        this.reservaEsporadicaService = reservaEsporadicaService;
        this.periodoRepository = periodoRepository;
        this.bedelRepository = bedelRepository;
    }

    @GetMapping
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<ReservaEsporadicaDTO>> getAllReservaEsporadicas() {
        return ResponseEntity.ok(reservaEsporadicaService.findAll());
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<ReservaEsporadicaDTO> getReservaEsporadica(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(reservaEsporadicaService.get(id));
    }

    @PostMapping("/disponibilidad")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<ReservaRespuestaDTO> getDisponibilidadAulaReservaEsporadica(
            @RequestBody @Valid final ReservaEsporadicaDTO reservaEsporadicaDTO) {
        ReservaRespuestaDTO reservaRespuestaDTO = reservaEsporadicaService.getDisponibilidadAulaReservaEsporadica(reservaEsporadicaDTO);
        return ResponseEntity.ok(reservaRespuestaDTO);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createReservaEsporadica(
            @RequestBody @Valid final ReservaEsporadicaDTO reservaEsporadicaDTO) {
        final Integer createdId = reservaEsporadicaService.create(reservaEsporadicaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Integer> updateReservaEsporadica(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ReservaEsporadicaDTO reservaEsporadicaDTO) {
        reservaEsporadicaService.update(id, reservaEsporadicaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReservaEsporadica(
            @PathVariable(name = "id") final Integer id) {
        reservaEsporadicaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/periodoValues")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<Integer, Integer>> getPeriodoValues() {
        return ResponseEntity.ok(periodoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Periodo::getId, Periodo::getId)));
    }

    @GetMapping("/bedelValues")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<Integer, Integer>> getBedelValues() {
        return ResponseEntity.ok(bedelRepository.findByEliminadoFalse(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Bedel::getId, Bedel::getId)));
    }

}
