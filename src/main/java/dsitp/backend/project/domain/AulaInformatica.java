package dsitp.backend.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@DiscriminatorValue("2")
@Data
@Component
public class AulaInformatica extends Aula {

    @Column
    private Integer cantidadPCs;

    @Column
    private Boolean tieneCanon;

}
