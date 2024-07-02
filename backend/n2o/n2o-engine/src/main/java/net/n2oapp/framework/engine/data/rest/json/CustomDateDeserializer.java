package net.n2oapp.framework.engine.data.rest.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Десериализиует даты, указанных форматов
 */
public class CustomDateDeserializer extends StdDeserializer<Object> {

    private List<DateTimeFormatter> formatters;
    private Set<String> exclusions;

    private final JsonDeserializer<?> defaultDeserializer;

    public CustomDateDeserializer(JsonDeserializer<?> defaultDeserializer) {
        super(Object.class);
        this.defaultDeserializer = defaultDeserializer;
    }

    public void setPatterns(String[] patterns) {
        if (ArrayUtils.isEmpty(patterns))
            return;

        List<DateTimeFormatter> formatters = new ArrayList<>();
        for (String pattern : patterns)
            formatters.add(DateTimeFormatter.ofPattern(pattern));

        this.formatters = formatters;
    }

    public void setExclusions(String[] exclusions) {
        this.exclusions = new HashSet<>(Arrays.asList(exclusions));
    }

    /**
     * Десериализует строку в дату, если она соответствует указанным форматам, если нет, то вызывается дефолтный десериализатор.
     */
    @Override
    public Object deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        String jsonString = jsonparser.getText();
        if (formatters == null || exclude(jsonparser.getCurrentName()))
            return defaultDeserializer.deserialize(jsonparser, context);
        try {
            return tryParse(jsonString);
        } catch (ParseException e) {
            return defaultDeserializer.deserialize(jsonparser, context);
        }
    }

    private boolean exclude(String key) {
        return exclusions != null
                && key != null
                && exclusions.contains(key);
    }

    private Object tryParse(String dateStr) throws ParseException {
        for (DateTimeFormatter formatter : formatters) {
            try {
                if (isValidDate(dateStr, formatter))
                    if (hasTime(dateStr, formatter))
                        return LocalDateTime
                                .from(formatter.parse(dateStr))
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    else
                        return LocalDate
                                .from(formatter.parse(dateStr))
                                .format(DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new ParseException("Unable to parse the date: " + dateStr, -1);
    }

    private boolean isValidDate(String dateStr, DateTimeFormatter formatter) {
        try {
            formatter.parse(dateStr);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    private boolean hasTime(String dateStr, DateTimeFormatter pattern) {
        try {
            LocalDateTime.parse(dateStr, pattern);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
