package com.effix.api.enums;

public enum PaymentInterval {
    MONTHLY("monthly"), YEARLY("yearly");

    private String interval;

    PaymentInterval(String interval) {
        this.interval=interval;
    }
    public String getInterval(){
        return this.interval;
    }
}
