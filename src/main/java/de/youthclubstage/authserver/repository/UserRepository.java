package de.youthclubstage.authserver.repository;

import de.youthclubstage.authserver.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends MongoRepository<User, UUID> {
    Optional<User> findByEmailAddress(String emailAddress);
}

