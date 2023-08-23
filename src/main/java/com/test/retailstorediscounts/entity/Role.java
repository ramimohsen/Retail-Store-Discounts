package com.test.retailstorediscounts.entity;


import com.test.retailstorediscounts.enums.UserRole;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Data
@Builder
public class Role {

    @Id
    private String id;
    private UserRole name;
}
