package dsitp.backend.project.rest;

import dsitp.backend.project.model.BedelDTO;
import dsitp.backend.project.service.BedelService;
import dsitp.backend.project.util.ReferencedException;
import dsitp.backend.project.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
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
    public ResponseEntity<List<BedelDTO>> getAllBedels() {
        return ResponseEntity.ok(bedelService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedelDTO> getBedel(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(bedelService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<?> createBedel(@RequestBody @Valid final BedelDTO bedelDTO, BindingResult result) {
        logger.info("Nuevo bedel es creado");
        bedelService.create(bedelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateBedel(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final BedelDTO bedelDTO) {
        bedelService.update(id, bedelDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBedel(@PathVariable(name = "id") final Integer id) {
        final ReferencedWarning referencedWarning = bedelService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        bedelService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
