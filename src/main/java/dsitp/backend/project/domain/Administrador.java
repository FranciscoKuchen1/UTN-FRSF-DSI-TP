package dsitp.backend.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "administrador", schema = "public")
@Getter
@Setter
@Component
public class Administrador extends Usuario {
}
