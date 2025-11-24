// com/example/demo/domain/Entities/User.java
package com.example.demo.domain.Entities;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.domain.Enums.Role;
import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;
    private String name;

    @Column(unique = true)
    private String email;

    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles;

    public User() {
    }

    public boolean hasRole(String role) {
        if (roles == null)
            return false;
        return roles.contains(role);
    }

    public User(Long id, Long companyId, String name, String email, String passwordHash, Set<Role> roles) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles = roles;
    }

    // --- getters & setters (same as before) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // --- UserDetails implementation ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_ADMIN, ROLE_TECHNICIAN, ...
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email; // we log in with email
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
}
