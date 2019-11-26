package de.youthclubstage.authserver.endpoint.model;

import lombok.Data;

@Data
public class AuthCheck {
    private String user;
    private String password;
}
