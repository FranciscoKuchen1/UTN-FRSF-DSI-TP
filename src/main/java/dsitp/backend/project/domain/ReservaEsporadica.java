package dsitp.backend.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "reservaEsporadica")
@Data
@Component
public class ReservaEsporadica extends Reserva {

}
