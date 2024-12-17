package dsitp.backend.project.rest;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.Periodo;
import dsitp.backend.project.model.ReservaDTO;
import dsitp.backend.project.model.ReservaRetornoDTO;
import dsitp.backend.project.repos.BedelRepository;
import dsitp.backend.project.repos.PeriodoRepository;
import dsitp.backend.project.service.ReservaService;
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
@RequestMapping(value = "/api/reservas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservaResource {

    private final ReservaService reservaService;
    private final PeriodoRepository periodoRepository;
    private final BedelRepository bedelRepository;

    public ReservaResource(final ReservaService reservaService,
            final PeriodoRepository periodoRepository, final BedelRepository bedelRepository) {
        this.reservaService = reservaService;
        this.periodoRepository = periodoRepository;
        this.bedelRepository = bedelRepository;
    }

    @PostMapping("/disponibilidad/{tipoReserva}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<ReservaRetornoDTO> getDisponibilidadAulaReserva(
            @RequestBody @Valid final ReservaDTO reservaDTO,
            @PathVariable(name = "tipoReserva") final Integer tipoReserva) {
        ReservaRetornoDTO reservaRetornoDTO = reservaService
                .getDisponibilidadAulaReserva(reservaDTO, tipoReserva);
        return ResponseEntity.ok(reservaRetornoDTO);
    }

    @PostMapping("/{tipoReserva}")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createReserva(
            @RequestBody @Valid final ReservaDTO reservaDTO,
            @PathVariable(name = "tipoReserva") final Integer tipoReserva) {
        final Integer createdId = reservaService.create(reservaDTO, tipoReserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdId);
    }

}
