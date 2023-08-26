package com.test.retailstorediscounts.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "discount_rules")
@Data
@Builder
public class DiscountRule {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Field("discount_parentage")
    private Double discountPercentage;

    @Field("discount_amount")
    private Double discountAmount;

    @Field("threshold")
    private Double threshold;

    private Boolean active;
}
