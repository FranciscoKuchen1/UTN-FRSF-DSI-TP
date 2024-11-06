package dsitp.backend.project.rest;

import dsitp.backend.project.model.AulaInformaticaDTO;
import dsitp.backend.project.service.AulaInformaticaService;
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
@RequestMapping(value = "/api/aulasInformaticas", produces = MediaType.APPLICATION_JSON_VALUE)
public class AulaInformaticaResource {

    private final AulaInformaticaService aulaInformaticaService;

    public AulaInformaticaResource(final AulaInformaticaService aulaInformaticaService) {
        this.aulaInformaticaService = aulaInformaticaService;
    }

    @GetMapping
    public ResponseEntity<List<AulaInformaticaDTO>> getAllAulaInformaticas() {
        return ResponseEntity.ok(aulaInformaticaService.findAll());
    }

    @GetMapping("/{numero}")
    public ResponseEntity<AulaInformaticaDTO> getAulaInformatica(
            @PathVariable(name = "numero") final Integer numero) {
        return ResponseEntity.ok(aulaInformaticaService.get(numero));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAulaInformatica(
            @RequestBody @Valid final AulaInformaticaDTO aulaInformaticaDTO) {
        final Integer createdNumero = aulaInformaticaService.create(aulaInformaticaDTO);
        return new ResponseEntity<>(createdNumero, HttpStatus.CREATED);
    }

    @PutMapping("/{numero}")
    public ResponseEntity<Integer> updateAulaInformatica(
            @PathVariable(name = "numero") final Integer numero,
            @RequestBody @Valid final AulaInformaticaDTO aulaInformaticaDTO) {
        aulaInformaticaService.update(numero, aulaInformaticaDTO);
        return ResponseEntity.ok(numero);
    }

    @DeleteMapping("/{numero}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAulaInformatica(
            @PathVariable(name = "numero") final Integer numero) {
        aulaInformaticaService.delete(numero);
        return ResponseEntity.noContent().build();
    }

}
