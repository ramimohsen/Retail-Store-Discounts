package com.test.retailstorediscounts.service.discount;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Component
@Setter
@Qualifier("amountRule")
public class AmountBasedRule implements Rule<DiscountDetails> {

    private static final String BILL_AMOUNT = "TOTAL_BILL_DISCOUNT_AMOUNT";

    private Double discountAmount;
    private Double threshold;
    private Double totalBillAmount;

    @Override
    public DiscountDetails calculate() {

        BigDecimal billAmount = BigDecimal.valueOf(this.totalBillAmount);
        BigDecimal thresholdValue = BigDecimal.valueOf(this.threshold);
        BigDecimal discountAmountValue = BigDecimal.valueOf(this.discountAmount);

        if (billAmount.compareTo(thresholdValue) >= 0) {
            BigDecimal timesThresholdExceeded = billAmount.divide(thresholdValue, 0, RoundingMode.DOWN);
            BigDecimal discount = timesThresholdExceeded.multiply(discountAmountValue);

            BigDecimal finalPrice = billAmount.subtract(discount);

            return DiscountDetails.builder()
                    .discountApplied(discount.doubleValue())
                    .finalPrice(finalPrice.setScale(2, RoundingMode.HALF_UP).doubleValue())
                    .originalPrice(billAmount.doubleValue())
                    .itemName(BILL_AMOUNT).build();
        } else {
            return DiscountDetails.builder()
                    .discountApplied(0.0)
                    .finalPrice(billAmount.setScale(2, RoundingMode.HALF_UP).doubleValue())
                    .originalPrice(billAmount.doubleValue())
                    .itemName(BILL_AMOUNT).build();
        }
    }
}
