package dsitp.backend.project.domain;

import dsitp.backend.project.model.TipoTurno;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bedel")
@Getter
@Setter
@Component
public class Bedel extends Usuario {

    @Column
    private Boolean eliminado;

    @Column
    private TipoTurno tipoTurno;

    @OneToMany(mappedBy = "bedel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

}
