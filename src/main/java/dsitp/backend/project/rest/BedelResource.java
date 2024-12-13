package dsitp.backend.project.rest;

import dsitp.backend.project.model.BedelDTO;
import dsitp.backend.project.service.BedelService;
import dsitp.backend.project.util.ReferencedException;
import dsitp.backend.project.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping(value = "/api/bedeles", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class BedelResource {

    private final BedelService bedelService;

    Logger logger = LoggerFactory.getLogger(BedelResource.class);

    public BedelResource(final BedelService bedelService) {
        this.bedelService = bedelService;
    }

    @GetMapping
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> getBedeles(@Min(0) @Max(2) @RequestParam(required = false) final Integer tipoTurno, @Size(max = 30) @RequestParam(required = false) final String apellido) {
        logger.info("Se buscan los bedeles por criterios");
        List<BedelDTO> bedelesDTO = bedelService.findBedeles(tipoTurno, apellido);
        if (bedelesDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Map.of("message", "No se encontraron bedeles con los criterios proporcionados"));
        }
        return ResponseEntity.ok(bedelesDTO);
    }

    @GetMapping("/{idRegistro}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<BedelDTO> getBedel(@PathVariable(name = "idRegistro") final String idRegistro) {
        // TODO: solucionar problema http status 500
        return ResponseEntity.ok(bedelService.getBedelByIdRegistro(idRegistro));
    }

    @GetMapping("/apellido/{apellido}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<BedelDTO>> getBedelesByApellido(@PathVariable(name = "apellido") final String apellido) {
        return ResponseEntity.ok(bedelService.getBedelesByApellido(apellido));
    }

    @GetMapping("/tipo-turno/{tipoTurno}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<BedelDTO>> getBedelesByTipoTurno(@PathVariable(name = "tipoTurno") final Integer tipoTurno) {
        return ResponseEntity.ok(bedelService.getBedelesByTipoTurno(tipoTurno));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<?> createBedel(@RequestBody @Valid final BedelDTO bedelDTO, BindingResult result) {
        logger.info("Nuevo bedel es creado");
        bedelService.create(bedelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PutMapping("/{idRegistro}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> updateBedel(@PathVariable(name = "idRegistro") final String idRegistro,
            @RequestBody @Valid final BedelDTO bedelDTO) {
        bedelService.update(idRegistro, bedelDTO);
        return ResponseEntity.ok(idRegistro);
    }

    @DeleteMapping("/{idRegistro}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBedelLogica(@PathVariable(name = "idRegistro") final String idRegistro) {
        logger.info("Bedel es eliminado");
        final ReferencedWarning referencedWarning = bedelService.getReferencedWarning(idRegistro);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }

        bedelService.deleteLogico(idRegistro);
        return ResponseEntity.noContent().build();
    }

}
