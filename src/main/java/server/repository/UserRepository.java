package server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "FROM User WHERE email = :email")
    User findByEmail(@Param("email") String email);
}
