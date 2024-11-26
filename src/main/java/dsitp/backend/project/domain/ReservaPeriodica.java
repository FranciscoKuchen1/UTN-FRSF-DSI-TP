package dsitp.backend.project.domain;

import dsitp.backend.project.model.TipoPeriodo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "reservaPeriodica")
@Data
@Component
public class ReservaPeriodica extends Reserva {

    @Column
    private TipoPeriodo tipoPeriodo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_periodo")
    private Periodo periodo;

}
