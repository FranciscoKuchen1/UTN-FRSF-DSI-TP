package dsitp.backend.project.rest;

import dsitp.backend.project.model.AulaMultimedioDTO;
import dsitp.backend.project.service.AulaMultimedioService;
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
@RequestMapping(value = "/api/aulasMultimedios", produces = MediaType.APPLICATION_JSON_VALUE)
public class AulaMultimedioResource {

    private final AulaMultimedioService aulaMultimedioService;

    public AulaMultimedioResource(final AulaMultimedioService aulaMultimedioService) {
        this.aulaMultimedioService = aulaMultimedioService;
    }

    @GetMapping
    public ResponseEntity<List<AulaMultimedioDTO>> getAllAulaMultimedios() {
        return ResponseEntity.ok(aulaMultimedioService.findAll());
    }

    @GetMapping("/{numero}")
    public ResponseEntity<AulaMultimedioDTO> getAulaMultimedio(
            @PathVariable(name = "numero") final Integer numero) {
        return ResponseEntity.ok(aulaMultimedioService.get(numero));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAulaMultimedio(
            @RequestBody @Valid final AulaMultimedioDTO aulaMultimedioDTO) {
        final Integer createdNumero = aulaMultimedioService.create(aulaMultimedioDTO);
        return new ResponseEntity<>(createdNumero, HttpStatus.CREATED);
    }

    @PutMapping("/{numero}")
    public ResponseEntity<Integer> updateAulaMultimedio(
            @PathVariable(name = "numero") final Integer numero,
            @RequestBody @Valid final AulaMultimedioDTO aulaMultimedioDTO) {
        aulaMultimedioService.update(numero, aulaMultimedioDTO);
        return ResponseEntity.ok(numero);
    }

    @DeleteMapping("/{numero}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAulaMultimedio(
            @PathVariable(name = "numero") final Integer numero) {
        aulaMultimedioService.delete(numero);
        return ResponseEntity.noContent().build();
    }

}
