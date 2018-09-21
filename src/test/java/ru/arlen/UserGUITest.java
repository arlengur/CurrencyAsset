package ru.arlen;

import com.google.gson.Gson;
import org.junit.Test;
import ru.arlen.dto.RateDto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.arlen.Constants.RUB;
import static ru.arlen.Constants.USD;
import static ru.arlen.utils.UserUtils.getCurrencyJSON;
import static ru.arlen.utils.UserUtils.getRateDTO;
public class UserGUITest {
    private static final String JSON = "{\"success\":true,\"timestamp\":1537519445,\"base\":\"EUR\",\"date\":\"2018-09-21\",\"rates\":{\"USD\":1.178516,\"RUB\":78.038941}}";

    @Test
    public void testParseJSONSuccess() {
        assertEquals(1.178516, getRateDTO(JSON).getRate(USD), 0.0);
        assertEquals(78.038941, getRateDTO(JSON).getRate(RUB), 0.0);
    }

    @Test(expected = RuntimeException.class)
    public void testParseJSONFail() {
        getRateDTO("{}");
    }

    @Test
    public void getCurrencyJSONSuccess() {
        String json = getCurrencyJSON("latest");
        RateDto rateDto = new Gson().fromJson(json, RateDto.class);
        assertTrue(rateDto.getSuccess());
    }
}
