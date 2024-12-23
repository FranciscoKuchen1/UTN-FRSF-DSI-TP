package dsitp.backend.project.domain;

import dsitp.backend.project.validation.FutureMoment;
import dsitp.backend.project.validation.RegistrarGroup;
import dsitp.backend.project.validation.ValidDuration;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dia_reservado", schema = "public")
@Getter
@Setter
@Component
@FutureMoment(message = "El momento debe ser futuro.")
public class DiaReservado {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(name = "primary_sequence", sequenceName = "primary_sequence", allocationSize = 1, initialValue = 10000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_sequence")
    private Integer id;

    @Column
    @NotNull(message = "La fecha de reserva es obligatoria.")
    private LocalDate fechaReserva;

    @Column
    @Schema(type = "string", example = "18:30")
    @NotNull(message = "La hora de inicio es obligatoria.")
    private LocalTime horaInicio;

    @Column
    @Schema(type = "string", example = "60")
    @ValidDuration
    @Positive
    @NotNull(message = "La duración es obligatoria.")
    private Integer duracion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aula")
    private Aula aula;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_reserva")
    @NotNull(message = "La reserva es obligatoria.")
    private Reserva reserva;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
