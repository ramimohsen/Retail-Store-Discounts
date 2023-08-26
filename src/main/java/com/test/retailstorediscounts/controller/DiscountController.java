package com.test.retailstorediscounts.controller;


import com.test.retailstorediscounts.dto.request.CalculateNetPayableRequest;
import com.test.retailstorediscounts.dto.response.CalculateNetPayableResponse;
import com.test.retailstorediscounts.dto.response.DiscountRuleResponse;
import com.test.retailstorediscounts.exception.custom.RuleEligibleException;
import com.test.retailstorediscounts.exception.custom.RuleNotFoundException;
import com.test.retailstorediscounts.exception.response.ErrorDetails;
import com.test.retailstorediscounts.service.discount.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Discount Controller", description = "Calculate net payable controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discount")
@Validated
public class DiscountController {

    private final DiscountService discountService;

    @Operation(summary = "Calculate net payable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CalculateNetPayableResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")}),
    })
    @PostMapping("/calculate")
    @PreAuthorize("hasAnyRole('EMPLOYEE','AFFILIATE','CUSTOMER')")
    public CalculateNetPayableResponse calculate(@RequestBody @Validated CalculateNetPayableRequest calculateNetPayableRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails) throws RuleEligibleException, RuleNotFoundException {
        return this.discountService.calculate(calculateNetPayableRequest, userDetails);
    }


    @Operation(summary = "Get eligible rules")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = DiscountRuleResponse.class), mediaType = "application/json")}),
    })
    @GetMapping("/eligible")
    @PreAuthorize("hasAnyRole('EMPLOYEE','AFFILIATE','CUSTOMER')")
    public List<DiscountRuleResponse> getEligible(@AuthenticationPrincipal UserDetails userDetails) {
        return this.discountService.getEligibleRules(userDetails);
    }

    @Operation(summary = "Get all available rules")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = DiscountRuleResponse.class), mediaType = "application/json")}),
    })
    @GetMapping("/rules")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DiscountRuleResponse> getAllRules() {
        return this.discountService.getAllRules();
    }

    @Operation(summary = "Get rule by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = DiscountRuleResponse.class), mediaType = "application/json")}),
    })
    @GetMapping("/rules/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DiscountRuleResponse getRuleById(@PathVariable("id") String id) throws RuleNotFoundException {
        return this.discountService.getRuleById(id);
    }
}
