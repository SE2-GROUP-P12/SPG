package it.polito.SE2.P12.SPG.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum ApplicationUserRole {
    CUSTOMER(Sets.newHashSet()),
    USER(Sets.newHashSet()),
    ADMIN(Sets.newHashSet()),
    EMPLOYEE(Sets.newHashSet()),
    FARMER(Sets.newHashSet());

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        //Map al the existent permissions instanced in the permissions enum file
        Set<SimpleGrantedAuthority> permissionsLocal = getPermissions().stream().map(
                        permission -> new SimpleGrantedAuthority(permission.getPermissions()))
                .collect(Collectors.toSet());
        permissionsLocal.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissionsLocal;

    }
}
