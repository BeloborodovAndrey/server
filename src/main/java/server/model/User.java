package server.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String lastname;

    @Column
    private Short age;

    @Column
    private String email;

    @Column
    private String password;

    @Transient
    private String rolesList;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER, cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
    }

    public User(Long id, String username, String lastname, Short age, String email, String password, String rolesList) {
        this.id = id;
        this.username = username;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.password = password;
        this.rolesList = rolesList;
    }

    public void addRoles(Set<Role> roles) {
        setRoles(roles);
        for (Role role : roles) {
            role.addUser(this);
        }
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setUserName(String name) {
        this.username = name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRolesList() {
        return rolesList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRolesForList() {
        rolesList = "";
        for (Role role: roles) {
            rolesList = rolesList + role.getName() + " ";
        }
    }

    public void setRolesList(String rolesList) {
        this.rolesList = rolesList;
    }
}
