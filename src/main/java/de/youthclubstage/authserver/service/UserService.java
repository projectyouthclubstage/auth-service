package de.youthclubstage.authserver.service;


import de.youthclubstage.authserver.entity.User;
import de.youthclubstage.authserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Value("${init.admin}")
    String admin;

    @Value("${init.password}")
    String password;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    public Optional<User> getUser(String email){
        return userRepository.findByEmailAddress(email);
    }

    public Optional<User> getUser(String email,String password)
    {
        Optional<User> user = userRepository.findByEmailAddress(email);
        if(user.isPresent() && encoder.matches(password,user.get().getPassword())){
            return user;
        }
        return Optional.empty();
    }

    public Optional<User> addOrganisation(String email, UUID organisation){
        Optional<User> user = userRepository.findByEmailAddress(email);
        if(!user.isPresent())
        {
            return Optional.empty();
        }
        userRepository.save(user.get());
        return user;
    }



    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return getUser(s).orElse(null);
    }

    @PostConstruct
    private void init(){
        if (userRepository.count() == 0 ) {
            User user = new User(UUID.randomUUID(),admin,"Sascha","Deeg","",true,password,true,"",true, Calendar.getInstance());
            user.setPassword(encoder.encode(password));
            userRepository.save(user);
        }
    }

}
