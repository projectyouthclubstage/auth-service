package de.youthclubstage.authserver.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",
            strategy = "uuid")
    private UUID id;

    private String emailAddress;

    private String firstName;
    private String lastName;
    private String password;

    private boolean is2fa;
    private String secret;


    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority gr = new SimpleGrantedAuthority("user");
        List<GrantedAuthority> result = new ArrayList<>();
        result.add(gr);
        return result;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.emailAddress;
    }


    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
