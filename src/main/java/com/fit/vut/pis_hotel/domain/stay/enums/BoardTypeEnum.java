package com.fit.vut.pis_hotel.domain.stay.enums;

public enum BoardTypeEnum {
    HALFBOARD(2000),
    FULLBOARD(4000);

    private final int price;

    BoardTypeEnum(int price) {
        this.price = price;
    }

    public int getPrice() {
        return this.price;
    }
}
