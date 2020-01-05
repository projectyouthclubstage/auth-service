package de.youthclubstage.authserver.endpoint.model;

import lombok.Data;

@Data
public class AuthCheckDto {
    private String user;
    private String password;
}
