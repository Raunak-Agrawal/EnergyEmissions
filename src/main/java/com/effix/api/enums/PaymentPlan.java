package com.effix.api.enums;

public enum PaymentPlan {
    STANDARD("standard"), PREMIUM("premium"), EXCLUSIVE("exclusive");

    private String plan;

    PaymentPlan(String plan) {
        this.plan=plan;
    }
    public String getPlan(){
        return this.plan;
    }
}
