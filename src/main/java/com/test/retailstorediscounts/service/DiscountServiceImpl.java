package com.test.retailstorediscounts.service;


import com.test.retailstorediscounts.dto.request.CalculateNetPayableRequest;
import com.test.retailstorediscounts.dto.response.CalculateNetPayableResponse;
import com.test.retailstorediscounts.dto.response.DiscountRuleResponse;
import com.test.retailstorediscounts.enums.UserRole;
import com.test.retailstorediscounts.repository.DiscountRuleRepository;
import com.test.retailstorediscounts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRuleRepository discountRuleRepository;

    private final UserRepository userRepository;

    @Override
    public CalculateNetPayableResponse calculate(CalculateNetPayableRequest calculateNetPayableRequest, UserDetails userDetails) {
        return null;
    }

    @Override
    public List<DiscountRuleResponse> getEligibleRules(UserDetails userDetails) {

        List<String> roleNames = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.substring("ROLE_".length()))
                .collect(Collectors.toList());

        if (roleNames.contains(UserRole.ROLE_CUSTOMER.cleanRolePrefix())
                && !this.userRepository.existsUserByEmailAndRegistrationDateBefore(userDetails.getUsername(), LocalDateTime.now()
                .minusYears(2).truncatedTo(ChronoUnit.SECONDS))) {
            roleNames.remove(UserRole.ROLE_CUSTOMER.cleanRolePrefix());
        }

        return discountRuleRepository.findByNameInAndActive(roleNames, true)
                .stream()
                .map(rule -> DiscountRuleResponse.builder()
                        .id(rule.getId())
                        .discountPercentage(rule.getDiscountPercentage())
                        .name(rule.getName())
                        .description(rule.getDescription()).build())
                .collect(Collectors.toList());
    }
}
