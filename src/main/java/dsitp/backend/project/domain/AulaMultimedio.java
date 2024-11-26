package dsitp.backend.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@DiscriminatorValue("3")
@Data
@Component
public class AulaMultimedio extends Aula {

    @Column
    private Boolean tieneTelevisor;

    @Column
    private Boolean tieneCanon;

    @Column
    private Boolean tieneComputadora;

    @Column
    private Boolean tieneVentiladores;

}
