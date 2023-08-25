package com.test.retailstorediscounts.service;

import com.test.retailstorediscounts.dto.response.DiscountRuleResponse;
import com.test.retailstorediscounts.entity.DiscountRule;
import com.test.retailstorediscounts.enums.UserRole;
import com.test.retailstorediscounts.repository.DiscountRuleRepository;
import com.test.retailstorediscounts.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}