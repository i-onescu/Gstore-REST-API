package com.gstore.gstoreapi.models.dtos;

import com.gstore.gstoreapi.enums.ProductCategory;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

@Builder
public record ProductDTO(@Nullable Long Id,
                         @Nullable @Pattern(regexp = "[a-zA-Z\\s]+") String name,
                         @Nullable @Pattern(regexp = "[0-9a-zA-Z]+") String manufacturer,
                         @Nullable @Pattern(regexp = "[a-zA-Z]+") String country,
                         @Nullable @Range(min = 1, max = 50000) Double price,
                         @Nullable Boolean available,
                         @Nullable @Range(min = 1, max = 10) Integer rating,
                         @Nullable Long sellerId,
                         @Nullable @Pattern(regexp = "[A-Z]+") String category) {
}

