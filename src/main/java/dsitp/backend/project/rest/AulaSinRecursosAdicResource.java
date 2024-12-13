package dsitp.backend.project.rest;

import dsitp.backend.project.model.AulaSinRecursosAdicDTO;
import dsitp.backend.project.service.AulaSinRecursosAdicService;
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
@RequestMapping(value = "/api/aulasSinRecursos", produces = MediaType.APPLICATION_JSON_VALUE)
public class AulaSinRecursosAdicResource {

    private final AulaSinRecursosAdicService aulaSinRecursosAdicService;

    public AulaSinRecursosAdicResource(
            final AulaSinRecursosAdicService aulaSinRecursosAdicService) {
        this.aulaSinRecursosAdicService = aulaSinRecursosAdicService;
    }

    @GetMapping
    public ResponseEntity<List<AulaSinRecursosAdicDTO>> getAllAulaSinRecursosAdics() {
        return ResponseEntity.ok(aulaSinRecursosAdicService.findAll());
    }

    @GetMapping("/{numero}")
    public ResponseEntity<AulaSinRecursosAdicDTO> getAulaSinRecursosAdic(
            @PathVariable(name = "numero") final Integer numero) {
        return ResponseEntity.ok(aulaSinRecursosAdicService.get(numero));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAulaSinRecursosAdic(
            @RequestBody @Valid final AulaSinRecursosAdicDTO aulaSinRecursosAdicDTO) {
        final Integer createdNumero = aulaSinRecursosAdicService.create(aulaSinRecursosAdicDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNumero);
    }

    @PutMapping("/{numero}")
    public ResponseEntity<Integer> updateAulaSinRecursosAdic(
            @PathVariable(name = "numero") final Integer numero,
            @RequestBody @Valid final AulaSinRecursosAdicDTO aulaSinRecursosAdicDTO) {
        aulaSinRecursosAdicService.update(numero, aulaSinRecursosAdicDTO);
        return ResponseEntity.ok(numero);
    }

    @DeleteMapping("/{numero}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAulaSinRecursosAdic(
            @PathVariable(name = "numero") final Integer numero) {
        aulaSinRecursosAdicService.delete(numero);
        return ResponseEntity.noContent().build();
    }

}
