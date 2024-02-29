package com.gstore.gstoreapi.models.dtos;

import jakarta.validation.constraints.NotNull;

public record LoginDTO(@NotNull String email,
                       @NotNull String password) { }
