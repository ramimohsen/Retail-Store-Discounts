package com.test.retailstorediscounts.repository;

import com.test.retailstorediscounts.entity.Role;
import com.test.retailstorediscounts.enums.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(UserRole name);

}
