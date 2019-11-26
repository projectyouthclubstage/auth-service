package de.youthclubstage.authserver.endpoint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserEndpoint {

    @RequestMapping({ "/user", "/me" })
    public Principal user(Principal principal) {
        return principal;
    }
}
