package ma.emsi.patientsmvc.security.userDetails.services;

import lombok.AllArgsConstructor;
import ma.emsi.patientsmvc.security.userDetails.entities.AppUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = accountService.loadUserByUsername(username);
        if (appUser==null) throw new UsernameNotFoundException(String.format("User %s not found",username));

        //String[] roles = appUser.getRoles().stream().map(u -> u.getRole()).toArray(String[]::new);


        List<SimpleGrantedAuthority> authorities = appUser
                .getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());

        UserDetails userDetails = User
                .withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                //.roles(roles)
                .authorities(authorities)
                .build();
        return userDetails;
    }
}