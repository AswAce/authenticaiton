package security.init;

import security.db.model.enums.RoleTypes;
import security.db.model.RoleEntity;
import security.db.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DbInit implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DbInit(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addRoles() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        Arrays.stream(RoleTypes.values()).forEach(roleTypes -> {
                    if (roleRepository.findByRole(roleTypes).orElse(null) == null) {
                        roleEntities.add(new RoleEntity(roleTypes));
                    }
                }
        );
        roleRepository.saveAll(roleEntities);

    }

    @Override
    public void run(String... args) {
        addRoles();
    }
}
