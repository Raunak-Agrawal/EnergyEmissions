package com.effix.api.enums;

import lombok.Getter;

@Getter
public enum VehicleType {
    CAR("car"), MOTORBIKE("motorbike"), TAXI("taxi"), BUS("bus"), RAIL("rail"), FLIGHT("flight");
    private final String name;

    VehicleType(String name) {
        this.name=name;
    }
    }
