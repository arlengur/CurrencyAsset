package ru.arlen.dto;

import java.util.Map;

public class RateDto {
    private boolean success = false;
    private Map<String, Double> rates = null;

    public boolean getSuccess() {
        return success;
    }

    public double getRate(String key) {
        return rates == null ? 0.0 : rates.get(key);
    }

    @Override
    public String toString() {
        return "Rate {" + "rates=" + rates + "}";
    }
}
