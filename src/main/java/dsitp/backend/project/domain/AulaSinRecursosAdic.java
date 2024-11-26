package dsitp.backend.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@DiscriminatorValue("1")
@Data
@Component
public class AulaSinRecursosAdic extends Aula {

    @Column
    private Boolean tieneVentiladores;

}
