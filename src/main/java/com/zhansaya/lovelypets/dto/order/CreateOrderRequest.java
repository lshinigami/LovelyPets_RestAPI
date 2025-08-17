package com.zhansaya.lovelypets.dto.order;

import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
        @NotNull Long customerId
) {}
