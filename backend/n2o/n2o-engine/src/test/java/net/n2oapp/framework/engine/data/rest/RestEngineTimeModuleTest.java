package net.n2oapp.framework.engine.data.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.engine.data.rest.json.RestEngineTimeModule;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RestEngineTimeModuleTest {

    @Test
    public void testSerializeDates() throws ParseException, IOException {
        ObjectMapper mapper = createObjectMapper();
        final Map<String, Object> model = new HashMap<>();
        model.put("id", 1);
        SimpleDateFormat defaultDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        model.put("date", defaultDateFormat.parse("01.11.1988"));
        DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        model.put("ld", LocalDate.parse("01.09.2017", localDateFormatter));
        model.put("ldt", LocalDateTime.parse("01.09.2017 12:00", localDateTimeFormatter));

        Map<String, Object> result = new ObjectMapper().readValue(mapper.writeValueAsString(model), Map.class);
        assertEquals(result.get("date"), "1988-11-01T00:00:00");
        assertEquals(result.get("ld"), "2017-09-01");
        assertEquals(result.get("id"), 1);
        assertEquals(result.get("ldt"), "2017-09-01T12:00:00");
    }

    @Test
    public void testDeserializeDates() throws Exception {
        ObjectMapper mapper = createObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("date", "01.01.2017 00:00");
        map.put("localDate1", "01.01.2017");
        map.put("localDate2", "01.01.2017 12:00");
        map.put("localDate3", "01.01.2017 12:00:00");
        map.put("localDate4", "2017-01-01T12:00:00");
        map.put("notdate", "not date");
        map.put("dateLength", "1234567890");
        map.put("ignore", "01.01.1000");

        Map res;
        res = mapper.readValue(new ObjectMapper().writeValueAsString(map), Map.class);
        assertEquals("2017-01-01T00:00:00", res.get("date"));
        assertEquals("2017-01-01", res.get("localDate1"));
        assertEquals("2017-01-01T12:00:00", res.get("localDate2"));
        assertEquals("2017-01-01T12:00:00", res.get("localDate3"));
        assertEquals("not date", res.get("notdate"));
        assertEquals("1234567890", res.get("dateLength"));
        assertEquals("01.01.1000", res.get("ignore"));
    }

    @Test
    public void testParseDateStrictly() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("phone", "8-800-3007300");

        map.put("validDate1", "2100-11-30");
        map.put("validDate2", "1-1-1");
        map.put("validDate3", "1945-02-02");

        map.put("invalidDate1", "1980-11-31");
        map.put("invalidDate2", "2050-13-15");
        map.put("invalidDate3", "2020-00-15");

        Map res = createObjectMapper().readValue(new ObjectMapper().writeValueAsString(map), Map.class);

        assertEquals("8-800-3007300", res.get("phone"));
        assertEquals(String.class, res.get("phone").getClass());

        assertEquals("2100-11-30", res.get("validDate1"));
        assertEquals("1-1-1", res.get("validDate2"));
        assertEquals("1945-02-02", res.get("validDate3"));

        assertEquals("1980-11-30", res.get("invalidDate1"));
        assertEquals("2050-13-15", res.get("invalidDate2"));
        assertEquals("2020-00-15", res.get("invalidDate3"));
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        String[] patterns = {"dd.MM.yyyy HH:mm", "dd.MM.yyyy", "dd.MM.yyyy HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
        String[] exclusions = {"ignore"};
        RestEngineTimeModule module = new RestEngineTimeModule(patterns, exclusions);
        objectMapper.registerModules(module);
        return objectMapper;
    }
}
