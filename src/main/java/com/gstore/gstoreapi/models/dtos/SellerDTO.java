package com.gstore.gstoreapi.models.dtos;

import com.gstore.gstoreapi.enums.AccountStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record SellerDTO(@Nullable @Pattern(regexp = "[0-9a-zA-Z ]+") String name,
                        @Nullable @Pattern(regexp = "[a-zA-Z]+") String country,
                        @Nullable @Pattern(regexp = ".+@.+\\..+") String email,
                        @Nullable Boolean international,
                        @Nullable @Pattern(regexp = "[A-Z]+") AccountStatus status) { }
