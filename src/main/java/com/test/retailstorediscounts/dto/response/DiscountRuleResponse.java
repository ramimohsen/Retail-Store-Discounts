package com.test.retailstorediscounts.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscountRuleResponse {

    private String id;

    private String name;

    private String description;

    private Double discountPercentage;

    private Double discountAmount;

    private Double threshold;
}
