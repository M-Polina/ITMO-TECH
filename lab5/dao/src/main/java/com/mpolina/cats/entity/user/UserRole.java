package com.mpolina.cats.entity.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Set;

public enum UserRole{
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");
    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role));
    }
}
