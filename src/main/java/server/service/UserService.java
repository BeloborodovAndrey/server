package server.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import server.model.Role;
import server.model.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {

    List<User> getUsersList();

    User getUserById(Long id);

    void addUser(User user);

    void updateUser(User user, String oldPassword);

    void deleteUser(User user);

    boolean validateData(String name, String lastname);

    Set<Role> getCurrentRoles(String requestRole);

}
