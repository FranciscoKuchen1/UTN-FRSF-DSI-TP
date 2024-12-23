package dsitp.backend.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Entity
@DiscriminatorValue("0")
@Getter
@Setter
@Component
public class AulaSinRecursosAdic extends Aula {

    @Column
    private Boolean tieneVentiladores;

}
