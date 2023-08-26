package com.test.retailstorediscounts.service.discount;


import com.test.retailstorediscounts.dto.request.CalculateNetPayableRequest;
import com.test.retailstorediscounts.dto.request.Item;
import com.test.retailstorediscounts.dto.response.CalculateNetPayableResponse;
import com.test.retailstorediscounts.dto.response.DiscountRuleResponse;
import com.test.retailstorediscounts.entity.DiscountRule;
import com.test.retailstorediscounts.enums.UserRole;
import com.test.retailstorediscounts.exception.custom.RuleEligibleException;
import com.test.retailstorediscounts.exception.custom.RuleNotFoundException;
import com.test.retailstorediscounts.repository.DiscountRuleRepository;
import com.test.retailstorediscounts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRuleRepository discountRuleRepository;

    private final UserRepository userRepository;

    private final Rule<DiscountDetails> amountRule;

    private final Rule<List<DiscountDetails>> discountRule;

    @Value("${retail.app.total.bill.discount.amount.rule.name:BILL100}")
    private String totalBillDiscountAmountRuleName;

    @Value("${retail.app.total.bill.discount.amount.rule.enabled:true}")
    private Boolean totalBillDiscountAmountRuleEnabled;

    public DiscountServiceImpl(DiscountRuleRepository discountRuleRepository, UserRepository userRepository,
                               @Qualifier("amountRule") Rule<DiscountDetails> amountRule,
                               @Qualifier("discountRule") Rule<List<DiscountDetails>> discountRule) {
        this.discountRuleRepository = discountRuleRepository;
        this.userRepository = userRepository;
        this.amountRule = amountRule;
        this.discountRule = discountRule;
    }


    @Override
    public CalculateNetPayableResponse calculate(CalculateNetPayableRequest calculateNetPayableRequest, UserDetails userDetails) throws RuleEligibleException, RuleNotFoundException {

        final String percentageRuleId = calculateNetPayableRequest.getPercentageRuleId();
        final Stack<DiscountRuleResponse> appliedRules = new Stack<>();

        if (percentageRuleId != null && !isUserEligibleForRule(percentageRuleId, userDetails)) {
            throw new RuleEligibleException("You are not eligible for this rule !");
        }

        List<DiscountDetails> discountDetails = this.applyPercentageRule(percentageRuleId, calculateNetPayableRequest, appliedRules);

        if (this.totalBillDiscountAmountRuleEnabled) {
            DiscountDetails amountDiscountDetails = this.applyDiscountAmountRule(discountDetails, appliedRules);

            return getCalculateNetPayableResponse(calculateNetPayableRequest,
                    appliedRules, discountDetails, amountDiscountDetails, amountDiscountDetails.getFinalPrice());
        }

        return getCalculateNetPayableResponse(calculateNetPayableRequest,
                appliedRules, discountDetails, null, discountDetails.stream()
                        .mapToDouble(DiscountDetails::getFinalPrice).sum());
    }


    @Override
    public List<DiscountRuleResponse> getEligibleRules(UserDetails userDetails) {

        List<String> roleNames = this.getRoleNames(userDetails);

        if (rolesContainsCustomer(roleNames) && !isLoyalCustomer(userDetails)) {
            roleNames.remove(UserRole.ROLE_CUSTOMER.cleanRolePrefix());
        }

        return discountRuleRepository.findByNameInAndActive(roleNames, true)
                .stream()
                .map(this::mapToRuleResponse)
                .toList();
    }

    @Override
    public List<DiscountRuleResponse> getAllRules() {
        return this.discountRuleRepository.findAll().stream().map(this::mapToRuleResponse).toList();
    }

    @Override
    public DiscountRuleResponse getRuleById(String id) throws RuleNotFoundException {
        return this.discountRuleRepository.findById(id)
                .map(this::mapToRuleResponse)
                .orElseThrow(() -> new RuleNotFoundException(String.format("Rule with id %s not found !", id)));
    }


    private List<DiscountDetails> applyPercentageRule(String percentageRuleId,
                                                      CalculateNetPayableRequest calculateNetPayableRequest
            , Stack<DiscountRuleResponse> rules) throws RuleNotFoundException {
        DiscountRuleResponse discountPercentageRule = this.getRuleById(percentageRuleId);
        ((DiscountBasedRule) discountRule).setItems(calculateNetPayableRequest.getItems());
        ((DiscountBasedRule) discountRule).setDiscountParentage(discountPercentageRule.getDiscountPercentage());
        rules.push(DiscountRuleResponse.builder()
                .threshold(discountPercentageRule.getThreshold())
                .discountAmount(discountPercentageRule.getDiscountAmount())
                .description(discountPercentageRule.getDescription())
                .id(discountPercentageRule.getId())
                .discountPercentage(discountPercentageRule.getDiscountPercentage())
                .name(discountPercentageRule.getName()).build());
        return discountRule.calculate();
    }

    private DiscountDetails applyDiscountAmountRule(List<DiscountDetails> discountDetails,
                                                    Stack<DiscountRuleResponse> rules) throws RuleNotFoundException {
        DiscountRule discountAmountRule = getRuleByName(this.totalBillDiscountAmountRuleName);
        ((AmountBasedRule) amountRule).setDiscountAmount(discountAmountRule.getDiscountAmount());
        ((AmountBasedRule) amountRule).setThreshold(discountAmountRule.getThreshold());
        ((AmountBasedRule) amountRule).setTotalBillAmount(discountDetails.stream()
                .mapToDouble(DiscountDetails::getFinalPrice).sum());
        rules.push(DiscountRuleResponse.builder()
                .threshold(discountAmountRule.getThreshold())
                .discountAmount(discountAmountRule.getDiscountAmount())
                .description(discountAmountRule.getDescription())
                .id(discountAmountRule.getId())
                .discountPercentage(discountAmountRule.getDiscountPercentage())
                .name(discountAmountRule.getName()).build());
        return amountRule.calculate();
    }

    private DiscountRule getRuleByName(String name) throws RuleNotFoundException {
        return this.discountRuleRepository
                .findByNameAndActive(name, true)
                .orElseThrow(() -> new RuleNotFoundException(String.format("Rule %s not found", name)));
    }

    private boolean isLoyalCustomer(UserDetails userDetails) {
        return this.userRepository.existsUserByEmailAndRegistrationDateBefore(userDetails.getUsername(),
                LocalDateTime.now()
                        .minusYears(2).truncatedTo(ChronoUnit.SECONDS));
    }

    private boolean rolesContainsCustomer(List<String> roleNames) {
        return roleNames.contains(UserRole.ROLE_CUSTOMER.cleanRolePrefix());
    }

    private List<String> getRoleNames(UserDetails userDetails) {

        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.substring("ROLE_".length()))
                .collect(Collectors.toList());
    }

    private boolean isUserEligibleForRule(String ruleId, UserDetails userDetails) {
        return getEligibleRules(userDetails)
                .stream()
                .map(DiscountRuleResponse::getId)
                .anyMatch(id -> id.equals(ruleId));
    }

    private CalculateNetPayableResponse getCalculateNetPayableResponse(CalculateNetPayableRequest calculateNetPayableRequest,
                                                                       Stack<DiscountRuleResponse> appliedRules,
                                                                       List<DiscountDetails> discountDetails,
                                                                       DiscountDetails amountDiscountDetails,
                                                                       Double amountDiscountDetails1) {
        return CalculateNetPayableResponse.builder()
                .originalBillAmount(calculateNetPayableRequest.getItems().stream().mapToDouble(Item::getPrice).sum())
                .rulesApplied(appliedRules)
                .itemsDiscountDetails(discountDetails)
                .discountOnTotalAmountDetails(amountDiscountDetails)
                .netPayableAmount(amountDiscountDetails1).build();
    }

    private DiscountRuleResponse mapToRuleResponse(DiscountRule rule) {
        return DiscountRuleResponse.builder()
                .id(rule.getId())
                .discountPercentage(rule.getDiscountPercentage())
                .name(rule.getName())
                .description(rule.getDescription())
                .discountAmount(rule.getDiscountAmount())
                .threshold(rule.getThreshold()).build();
    }
}
