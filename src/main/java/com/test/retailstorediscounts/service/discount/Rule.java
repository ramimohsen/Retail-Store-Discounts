package com.test.retailstorediscounts.service.discount;

import org.springframework.stereotype.Component;

@Component
public interface Rule<T> {
    T calculate();
}
