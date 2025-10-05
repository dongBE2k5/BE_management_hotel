package tdc.vn.managementhotel.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tdc.vn.managementhotel.entity.Role;
import tdc.vn.managementhotel.entity.User;

import java.util.Collection;
import java.util.Collections;

@Data
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Role role;

    public CustomUserDetails(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = (this.role != null) ? this.role.getName() : "ROLE_USER";
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return password;   // dùng field trực tiếp
    }

    @Override
    public String getUsername() {
        return username;   // dùng field trực tiếp
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public Long getId() {
        return id;  // dùng field trực tiếp
    }

    public Role getRole() {
        return role;  // dùng field trực tiếp
    }
}
