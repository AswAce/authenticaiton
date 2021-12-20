package security.db.model;

import security.db.model.enums.RoleTypes;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleTypes role;

    public RoleEntity(RoleTypes role) {
        this.role = role;
    }

    public RoleTypes getRole() {
        return role;
    }
}
