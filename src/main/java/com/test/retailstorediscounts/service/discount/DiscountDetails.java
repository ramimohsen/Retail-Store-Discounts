package com.test.retailstorediscounts.service.discount;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DiscountDetails {

    private Double finalPrice;
    private Double discountApplied;
    private Double originalPrice;
    private String itemName;

}
