package com.test.retailstorediscounts.repository;

import com.test.retailstorediscounts.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findUserByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsUserByEmailAndRegistrationDateBefore(String email, LocalDateTime localDateTime);
}
