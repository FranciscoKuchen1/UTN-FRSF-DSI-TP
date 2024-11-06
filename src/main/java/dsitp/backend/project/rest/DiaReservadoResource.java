package dsitp.backend.project.rest;

import dsitp.backend.project.model.DiaReservadoDTO;
import dsitp.backend.project.service.DiaReservadoService;
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
@RequestMapping(value = "/api/diasReservados", produces = MediaType.APPLICATION_JSON_VALUE)
public class DiaReservadoResource {

    private final DiaReservadoService diaReservadoService;

    public DiaReservadoResource(final DiaReservadoService diaReservadoService) {
        this.diaReservadoService = diaReservadoService;
    }

    @GetMapping
    public ResponseEntity<List<DiaReservadoDTO>> getAllDiaReservados() {
        return ResponseEntity.ok(diaReservadoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaReservadoDTO> getDiaReservado(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(diaReservadoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createDiaReservado(
            @RequestBody @Valid final DiaReservadoDTO diaReservadoDTO) {
        final Long createdId = diaReservadoService.create(diaReservadoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateDiaReservado(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final DiaReservadoDTO diaReservadoDTO) {
        diaReservadoService.update(id, diaReservadoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDiaReservado(@PathVariable(name = "id") final Long id) {
        diaReservadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
