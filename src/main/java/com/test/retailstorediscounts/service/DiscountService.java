package com.test.retailstorediscounts.service;

import com.test.retailstorediscounts.dto.request.CalculateNetPayableRequest;
import com.test.retailstorediscounts.dto.response.CalculateNetPayableResponse;
import com.test.retailstorediscounts.dto.response.DiscountRuleResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface DiscountService {

    CalculateNetPayableResponse calculate(CalculateNetPayableRequest calculateNetPayableRequest, UserDetails userDetails);

    List<DiscountRuleResponse> getEligibleRules(UserDetails userDetails);

}
