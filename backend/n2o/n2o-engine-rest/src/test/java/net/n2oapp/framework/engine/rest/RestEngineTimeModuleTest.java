package net.n2oapp.framework.engine.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.engine.rest.json.RestEngineTimeModule;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        Assert.assertEquals(result.get("date"), "1988-11-01T00:00:00");
        Assert.assertEquals(result.get("ld"), "2017-09-01");
        Assert.assertEquals(result.get("id"), 1);
        Assert.assertEquals(result.get("ldt"), "2017-09-01T12:00:00");
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

        Map res;
        res = mapper.readValue(new ObjectMapper().writeValueAsString(map), Map.class);
        Assert.assertEquals(Date.class, res.get("date").getClass());
        Assert.assertEquals(Date.class, res.get("localDate1").getClass());
        Assert.assertEquals(Date.class, res.get("localDate2").getClass());
        Assert.assertEquals(Date.class, res.get("localDate3").getClass());
        Assert.assertEquals(String.class, res.get("notdate").getClass());
        Assert.assertEquals(String.class, res.get("dateLength").getClass());
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        RestEngineTimeModule module = new RestEngineTimeModule(new String[]{"dd.MM.yyyy HH:mm", "dd.MM.yyyy", "dd.MM.yyyy HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss"});
        objectMapper.registerModules(module);
        return objectMapper;
    }
}
