package dsitp.backend.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Entity
@DiscriminatorValue("2")
@Getter
@Setter
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
