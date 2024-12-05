package dsitp.backend.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "reservaPeriodica")
@Getter
@Setter
@Component
public class ReservaPeriodica extends Reserva {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_periodo")
    private Periodo periodo;

}
