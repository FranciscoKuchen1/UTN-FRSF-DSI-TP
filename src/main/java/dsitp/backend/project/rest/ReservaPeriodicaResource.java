package dsitp.backend.project.rest;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.Periodo;
import dsitp.backend.project.model.ReservaPeriodicaDTO;
import dsitp.backend.project.model.ReservaPeriodicaSinDiasDTO;
import dsitp.backend.project.model.ReservaRetornoDTO;
import dsitp.backend.project.repos.BedelRepository;
import dsitp.backend.project.repos.PeriodoRepository;
import dsitp.backend.project.service.ReservaPeriodicaService;
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
@RequestMapping(value = "/api/reservasPeriodicas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservaPeriodicaResource {

    private final ReservaPeriodicaService reservaPeriodicaService;
    private final PeriodoRepository periodoRepository;
    private final BedelRepository bedelRepository;

    public ReservaPeriodicaResource(final ReservaPeriodicaService reservaPeriodicaService,
            final PeriodoRepository periodoRepository, final BedelRepository bedelRepository) {
        this.reservaPeriodicaService = reservaPeriodicaService;
        this.periodoRepository = periodoRepository;
        this.bedelRepository = bedelRepository;
    }

    @GetMapping
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<ReservaPeriodicaDTO>> getAllReservaPeriodicas() {
        return ResponseEntity.ok(reservaPeriodicaService.findAll());
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<ReservaPeriodicaDTO> getReservaPeriodica(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(reservaPeriodicaService.get(id));
    }

    // @PostMapping("/disponibilidad")
    // @ApiResponse(responseCode = "200")
    // public ResponseEntity<ReservaRetornoDTO>
    // getDisponibilidadAulaReservaPeriodica(
    // @RequestBody @Valid final ReservaPeriodicaSinDiasDTO
    // reservaPeriodicaSinDiasDTO) {
    // ReservaRetornoDTO reservaRetornoDTO = reservaPeriodicaService
    // .getDisponibilidadAulaReservaPeriodica(reservaPeriodicaSinDiasDTO);
    // return ResponseEntity.ok(reservaRetornoDTO);
    // }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createReservaPeriodica(
            @RequestBody @Valid final ReservaPeriodicaDTO reservaPeriodicaDTO) {
        final Integer createdId = reservaPeriodicaService.create(reservaPeriodicaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdId);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Integer> updateReservaPeriodica(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final ReservaPeriodicaDTO reservaPeriodicaDTO) {
        reservaPeriodicaService.update(id, reservaPeriodicaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReservaPeriodica(
            @PathVariable(name = "id") final Integer id) {
        reservaPeriodicaService.delete(id);
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
