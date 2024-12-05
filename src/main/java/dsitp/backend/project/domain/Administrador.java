package dsitp.backend.project.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Entity
@Getter
@Setter
@Component
public class Administrador extends Usuario {
}
