package com.test.retailstorediscounts.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalculateNetPayableResponse {

    private double originalBillAmount;
    private List<String> discountsApplied;
    private double netPayableAmount;

}
