package com.test.retailstorediscounts.service.discount;

import com.test.retailstorediscounts.dto.request.Item;
import com.test.retailstorediscounts.enums.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Component
@Setter
@Qualifier("discountRule")
public class DiscountBasedRule implements Rule<List<DiscountDetails>> {

    private Double discountParentage;
    private List<Item> items;

    @Override
    public List<DiscountDetails> calculate() {

        return this.items.stream()
                .map(item -> {
                    BigDecimal itemPrice = BigDecimal.valueOf(item.getPrice());
                    DiscountDetails discountDetails = DiscountDetails.builder()
                            .itemName(item.getName())
                            .originalPrice(item.getPrice()).build();

                    if (!item.getCategory().equals(Category.GROCERY)) {
                        BigDecimal discountPercentageValue = BigDecimal.valueOf(this.discountParentage);
                        BigDecimal discount = itemPrice.multiply(discountPercentageValue
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                        discountDetails.setDiscountApplied(discount.doubleValue());
                        discountDetails.setFinalPrice(itemPrice.subtract(discount).doubleValue());
                    } else {
                        discountDetails.setDiscountApplied(0.0);
                        discountDetails.setFinalPrice(item.getPrice());
                    }
                    return discountDetails;
                })
                .collect(Collectors.toList());
    }
}
