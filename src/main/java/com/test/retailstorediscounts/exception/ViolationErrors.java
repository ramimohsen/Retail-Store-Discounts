package com.test.retailstorediscounts.exception;


import lombok.Builder;

@Builder
public record ViolationErrors(String fieldName, String message) {
}
