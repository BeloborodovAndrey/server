package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import server.model.Role;
import server.model.User;
import server.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/")
public class UsersRestController {

    UserService userService;

    @Autowired
    public UsersRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String start(Authentication authentication) {
        String userName = authentication.getName();
        return "Server is running - " + userName;
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getUsersList() {
        List<User> users = userService.getUsersList();
        for (User user: users) {
            user.setRolesForList();
            user.setRoles(null);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/admin/add")
    public ResponseEntity addUser(@RequestBody User requestUser) {
        Set<Role> roles = userService.getCurrentRoles(requestUser.getRolesList());
        requestUser.addRoles(roles);
        if (userService.validateData(requestUser.getUsername(), requestUser.getPassword())) {
            userService.addUser(requestUser);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/admin/editUser")
    public ResponseEntity<User> getUpdateData(@RequestBody User requestUser) {
        User user = userService.getUserById(requestUser.getId());
        user.setRolesForList();
        user.setRoles(null);
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @PostMapping("/admin/edit")
    public ResponseEntity updateUser(@RequestBody User user) {
        User oldUser = userService.getUserById(user.getId());
        String oldPassword = oldUser.getPassword();
        if (!user.getPassword().equals("")) {
            user.setPassword(user.getPassword());
        } else {
            user.setPassword(oldPassword);
        }
        if (userService.validateData(user.getUsername(), user.getPassword())) {
            user.addRoles(userService.getCurrentRoles(user.getRolesList().trim()));
            userService.updateUser(user, user.getPassword());
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/admin/delete")
    public ResponseEntity deleteUser(@RequestBody String id) {
        Long userId = Long.valueOf(id);
        User user = userService.getUserById(userId);
        userService.deleteUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/admin/getUser")
    public ResponseEntity<User> getUser(@RequestBody User requestUser) {
        UserDetails user = userService.loadUserByUsername(requestUser.getEmail());
        return new ResponseEntity(user, HttpStatus.OK);
    }
}
