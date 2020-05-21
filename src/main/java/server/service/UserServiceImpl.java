package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.model.Role;
import server.model.User;
import server.repository.RoleRepository;
import server.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public List<User> getUsersList() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void addUser(User user) {
        try {
            User userFromDB = userRepository.findByEmail(user.getEmail());
            if (userFromDB == null) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void updateUser(User user, String oldPassword) {
        if (!user.getPassword().equals(oldPassword)) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public boolean validateData(String name, String lastname) {
        return !(name.isEmpty() || lastname.isEmpty());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByEmail(email);
        return userDetails;
    }

    @Override
    public Set<Role> getCurrentRoles(String requestRole) {
        Set<Role> roles = new HashSet<>();
        if (requestRole.equals("ALL")) {
            Role roleUser = getRoleByName("USER");
            Role roleAdmin = getRoleByName("ADMIN");
            roles.add(roleUser == null ? new Role("USER") : roleUser);
            roles.add(roleAdmin == null ? new Role("ADMIN") : roleAdmin);
        } else {
            Role role = getRoleByName(requestRole);
            roles.add(role == null ? new Role(requestRole) : role);
        }
        return roles;
    }

    private Role getRoleByName(String roleName) {
        return roleRepository.getRoleByName(roleName);
    }

}
