package ru.arlen.utils;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import ru.arlen.dto.RateDto;

import java.io.IOException;

import static ru.arlen.Constants.*;

public class UserUtils {
    private static HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();

    public static double calculateProfit(String pastDate, String amountStr, Logger logger) {
        double pastAmount = getAmount(pastDate, false, amountStr, logger);
        double nowAmount = getAmount("latest", true, amountStr, logger);
        return nowAmount - pastAmount;
    }

    /**
     * Calculates amount RUB for date and spread if needed.
     *
     * @param date   date when currency calculates
     * @param spread is the spread calculates
     * @return amount RUB
     */
    private static double getAmount(String date, boolean spread, String amountStr, Logger logger) {
        double unitCurrency = getUnitCurrency(date, spread);
        double amount = unitCurrency * Integer.parseInt(amountStr);
        logger.info(date + " 1 RUB = " + unitCurrency);
        logger.info("Total RUB = " + amount);
        return amount;
    }

    private static double getUnitCurrency(String date, boolean spread) {
        String response = getCurrencyJSON(date);
        RateDto rate = getRateDTO(response);
        double amount = rate.getRate(RUB) / rate.getRate(USD);
        if (spread) {
            amount -= amount * SPREAD;
        }
        return amount;
    }

    public static String getCurrencyJSON(String date) {
        try {
            HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(String.format(URL, date)));
            return request.execute().parseAsString();
        } catch (IOException e) {
            throw new RuntimeException("Transfer error.");
        }
    }

    public static RateDto getRateDTO(String response) {
        RateDto rate = new Gson().fromJson(response, RateDto.class);
        if (!rate.getSuccess()) {
            throw new RuntimeException("Parse error: " + response);
        }
        return rate;
    }
}
