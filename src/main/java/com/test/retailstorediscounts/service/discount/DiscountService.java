package com.test.retailstorediscounts.service.discount;

import com.test.retailstorediscounts.dto.request.CalculateNetPayableRequest;
import com.test.retailstorediscounts.dto.response.CalculateNetPayableResponse;
import com.test.retailstorediscounts.dto.response.DiscountRuleResponse;
import com.test.retailstorediscounts.exception.custom.RuleEligibleException;
import com.test.retailstorediscounts.exception.custom.RuleNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface DiscountService {

    CalculateNetPayableResponse calculate(CalculateNetPayableRequest calculateNetPayableRequest, UserDetails userDetails) throws RuleEligibleException, RuleNotFoundException;


    /**
     * @param userDetails auth user details
     * @return @{@link List<DiscountRuleResponse>} List of users applicable rules
     */
    List<DiscountRuleResponse> getEligibleRules(UserDetails userDetails);


    List<DiscountRuleResponse> getAllRules();

    DiscountRuleResponse getRuleById(String id) throws RuleNotFoundException;


}
