package tdc.vn.managementhotel.dto.UserDTO;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tdc.vn.managementhotel.entity.Role;

import java.util.Collection;

@Data
public abstract class UserLoginResponse implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Role role;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
