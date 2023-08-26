package com.test.retailstorediscounts.dto.response;


import com.test.retailstorediscounts.service.discount.DiscountDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Stack;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalculateNetPayableResponse {

    private double originalBillAmount;
    private double netPayableAmount;
    private Stack<DiscountRuleResponse> rulesApplied;
    private List<DiscountDetails> itemsDiscountDetails;
    private DiscountDetails discountOnTotalAmountDetails;

}
