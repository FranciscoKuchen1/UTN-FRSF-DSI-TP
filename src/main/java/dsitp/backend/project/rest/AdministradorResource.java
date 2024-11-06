package dsitp.backend.project.rest;

import dsitp.backend.project.model.AdministradorDTO;
import dsitp.backend.project.service.AdministradorService;
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
@RequestMapping(value = "/api/administradores", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministradorResource {

    private final AdministradorService administradorService;

    public AdministradorResource(final AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @GetMapping
    public ResponseEntity<List<AdministradorDTO>> getAllAdministradors() {
        return ResponseEntity.ok(administradorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorDTO> getAdministrador(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(administradorService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAdministrador(
            @RequestBody @Valid final AdministradorDTO administradorDTO) {
        final Integer createdId = administradorService.create(administradorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateAdministrador(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final AdministradorDTO administradorDTO) {
        administradorService.update(id, administradorDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAdministrador(@PathVariable(name = "id") final Integer id) {
        administradorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
