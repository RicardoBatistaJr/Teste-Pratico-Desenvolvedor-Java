package com.teste.pratico.domain.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum SpotTypeEnum {
    VIP(35),
    COMMON(10);

    private final long maintenanceFeeRate;

    SpotTypeEnum(long maintenanceFeeRate) {
        this.maintenanceFeeRate = maintenanceFeeRate;
    }

}

