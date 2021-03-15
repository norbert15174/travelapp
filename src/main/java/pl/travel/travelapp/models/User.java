package pl.travel.travelapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class User implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Min(5)
    @Max(20)
    private String login;
    @Min(8)
    @Max(20)
    private String password;
    private Role role = Role.ROLE_ADMIN;
    private boolean isEnable = false;
    @Email
    private String email;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login;
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
        return this.isEnable;
    }
}
