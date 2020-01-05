package de.youthclubstage.authserver.endpoint;

import de.youthclubstage.authserver.endpoint.model.AuthCheckDto;
import de.youthclubstage.authserver.entity.User;
import de.youthclubstage.authserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TwoFaCheckEndpoint {

    private final UserService userService;

    @Autowired
    public TwoFaCheckEndpoint(UserService userService){
        this.userService = userService;
    }

    @PostMapping( value = "2fa", consumes = "application/json;charset=UTF-8")
    ResponseEntity<Void> hasUser2Fa(@RequestBody AuthCheckDto authCheck){
        Optional<User> user = userService.getUser(authCheck.getUser(),authCheck.getPassword());
        if(user.isPresent() && user.get().is2fa()){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
