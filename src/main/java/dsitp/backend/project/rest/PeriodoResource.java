package dsitp.backend.project.rest;

import dsitp.backend.project.model.PeriodoDTO;
import dsitp.backend.project.service.PeriodoService;
import dsitp.backend.project.util.ReferencedException;
import dsitp.backend.project.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping(value = "/api/periodos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PeriodoResource {

    private final PeriodoService periodoService;

    public PeriodoResource(final PeriodoService periodoService) {
        this.periodoService = periodoService;
    }

    @GetMapping
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<PeriodoDTO>> getAllPeriodos() {
        return ResponseEntity.ok(periodoService.findAll());
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<PeriodoDTO> getPeriodo(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(periodoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createPeriodo(@RequestBody @Valid final PeriodoDTO periodoDTO) {
        final Integer createdId = periodoService.create(periodoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Integer> updatePeriodo(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final PeriodoDTO periodoDTO) {
        periodoService.update(id, periodoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePeriodo(@PathVariable(name = "id") final Integer id) {
        final ReferencedWarning referencedWarning = periodoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        periodoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
