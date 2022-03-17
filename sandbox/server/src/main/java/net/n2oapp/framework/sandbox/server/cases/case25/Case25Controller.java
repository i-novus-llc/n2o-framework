package net.n2oapp.framework.sandbox.server.cases.case25;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Case25Controller {

    public static List<Map> getList(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("date", new Date());
        map.put("localDateTime", LocalDateTime.now());
        map.put("localDate", LocalDate.now());
        map.put("localTime", LocalTime.now());
        map.put("num", new BigDecimal("2.5"));
        return Collections.singletonList(map);
    }

    public static class Test {
        @JsonProperty
        public LocalDateTime date;
    }
}
