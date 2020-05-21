package server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "from Role where name = :role_name")
    Role getRoleByName(@Param("role_name") String name);
}
