package com.test.retailstorediscounts.service;

import com.test.retailstorediscounts.dto.response.DiscountRuleResponse;
import com.test.retailstorediscounts.entity.DiscountRule;
import com.test.retailstorediscounts.enums.UserRole;
import com.test.retailstorediscounts.exception.custom.RuleNotFoundException;
import com.test.retailstorediscounts.repository.DiscountRuleRepository;
import com.test.retailstorediscounts.repository.UserRepository;
import com.test.retailstorediscounts.service.discount.DiscountService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class DiscountServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private DiscountRuleRepository discountRuleRepository;

    @Autowired
    private DiscountService discountRuleService;

    @Test
    public void testGetEligibleRulesForCustomerNotRegisteredForMoreThan2Years() {

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        org.springframework.security.core.userdetails.UserDetails userDetails =
                new User("testUser", "password", authorities);

        when(userRepository.existsUserByEmailAndRegistrationDateBefore("testUser", LocalDateTime.now().minusYears(2)))
                .thenReturn(false);

        when(discountRuleRepository.findByNameInAndActive(anyList(), eq(true)))
                .thenReturn(List.of());

        List<DiscountRuleResponse> eligibleRules = discountRuleService.getEligibleRules(userDetails);

        assertEquals(0, eligibleRules.size());
        verify(userRepository, times(1))
                .existsUserByEmailAndRegistrationDateBefore("testUser", LocalDateTime.now()
                        .minusYears(2)
                        .truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    public void testGetEligibleRulesForCustomerRegisteredForMoreThan2Years() {

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        org.springframework.security.core.userdetails.UserDetails userDetails =
                new User("testUser", "password", authorities);

        when(userRepository.existsUserByEmailAndRegistrationDateBefore("testUser", LocalDateTime.now().minusYears(2)))
                .thenReturn(true);

        when(discountRuleRepository.findByNameInAndActive(anyList(), eq(true)))
                .thenReturn(List.of(DiscountRule.builder()
                        .name("CUSTOMER").build()));

        List<DiscountRuleResponse> eligibleRules = discountRuleService.getEligibleRules(userDetails);

        assertEquals(1, eligibleRules.size());
        assertEquals(UserRole.ROLE_CUSTOMER.cleanRolePrefix(), eligibleRules.get(0).getName());

        verify(userRepository, times(1))
                .existsUserByEmailAndRegistrationDateBefore("testUser", LocalDateTime.now()
                        .minusYears(2)
                        .truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    public void testGetAllRules() {

        List<DiscountRule> sampleRules = List.of(
                DiscountRule.builder()
                        .name("CUSTOMER").id("1").build(),
                DiscountRule.builder()
                        .name("EMPLOYEE").id("2").build(),
                DiscountRule.builder()
                        .name("Bill100").id("3").build());

        when(discountRuleRepository.findAll()).thenReturn(sampleRules);

        List<DiscountRuleResponse> ruleResponses = discountRuleService.getAllRules();

        assertEquals(sampleRules.size(), ruleResponses.size());
    }

    @SneakyThrows
    @Test
    public void testGetRuleById_WhenRuleExists_ShouldReturnRuleResponse() {
        String ruleId = "64e86e443f3c9e18852e3350";
        DiscountRule expectedRule = DiscountRule.builder().name("EMPLOYEE").id(ruleId)
                .discountPercentage(30.0).build();

        when(discountRuleRepository.findById(ruleId)).thenReturn(Optional.of(expectedRule));

        DiscountRuleResponse ruleResponse = discountRuleService.getRuleById(ruleId);

        assertNotNull(ruleResponse);
        assertEquals(ruleId, ruleResponse.getId());
        assertEquals("EMPLOYEE", ruleResponse.getName());
        assertEquals(30, ruleResponse.getDiscountPercentage());
    }

    @Test
    public void testGetRuleById_WhenRuleDoesNotExist_ShouldThrowException() {
        String nonExistentRuleId = "nonexistent-rule-id";

        when(discountRuleRepository.findById(nonExistentRuleId)).thenReturn(Optional.empty());

        assertThrows(RuleNotFoundException.class, () -> discountRuleService.getRuleById(nonExistentRuleId));
    }
}