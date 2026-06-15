package com.homestay.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderRequest {
    @NotNull(message = "房间ID不能为空")
    private Long roomId;

    @NotNull(message = "入住日期不能为空")
    private LocalDate checkIn;

    @NotNull(message = "离店日期不能为空")
    private LocalDate checkOut;
}
