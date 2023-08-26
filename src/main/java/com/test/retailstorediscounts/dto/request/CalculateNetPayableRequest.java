package com.test.retailstorediscounts.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateNetPayableRequest {

    @NotEmpty
    private List<Item> items;

    private String percentageRuleId;
}
