package com.test.retailstorediscounts.dto.request;

import com.test.retailstorediscounts.enums.Category;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @NotNull
    private String name;
    @NotNull
    private Double price;
    @NotNull
    private Category category;

}
