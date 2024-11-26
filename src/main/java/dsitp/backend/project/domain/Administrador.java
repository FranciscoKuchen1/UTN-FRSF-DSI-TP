package dsitp.backend.project.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@Data
@Component
public class Administrador extends Usuario {
}
