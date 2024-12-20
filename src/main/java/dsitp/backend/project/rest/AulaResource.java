package dsitp.backend.project.rest;

import dsitp.backend.project.model.AulaDTO;
import dsitp.backend.project.service.AulaService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/aulas", produces = MediaType.APPLICATION_JSON_VALUE)
public class AulaResource {

    private final AulaService aulaService;

    Logger logger = LoggerFactory.getLogger(AulaResource.class);

    public AulaResource(final AulaService aulaService) {
        this.aulaService = aulaService;
    }

//    @GetMapping
//    public ResponseEntity<List<AulaDTO>> getAllAulas() {
//        return ResponseEntity.ok(aulaService.findAll());
//    }
    @GetMapping("/{numero}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<AulaDTO> getAula(
            @PathVariable(name = "numero") final Integer numero) {
        return ResponseEntity.ok(aulaService.get(numero));
    }

    @GetMapping
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<AulaDTO>> getAulas(@RequestParam(required = false) Integer numero, @RequestParam(required = false) Integer tipoAula, @RequestParam(required = false) Integer capacidad) {
        List<AulaDTO> aulasDTO = aulaService.findAulas(numero, tipoAula, capacidad);
        return ResponseEntity.ok(aulasDTO);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAula(@RequestBody @Valid final AulaDTO aulaDTO) {
        final Integer createdNumero = aulaService.create(aulaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNumero);
    }

    @PutMapping("/{numero}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Integer> updateAula(@PathVariable(name = "numero") final Integer numero, @RequestBody @Valid final AulaDTO aulaDTO) {
        aulaService.update(numero, aulaDTO);
        return ResponseEntity.ok(numero);
    }

    @DeleteMapping("/{numero}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAula(@PathVariable(name = "numero") final Integer numero) {
        aulaService.delete(numero);
        return ResponseEntity.noContent().build();
    }

}
