package net.n2oapp.framework.engine.data.rest.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.lang.time.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Десериализиует даты, указанных форматов
 */
public class CustomDateDeserializer extends StdDeserializer<Object> {

    private String[] patterns;
    private Set<String> exclusions;

    private final JsonDeserializer<?> defaultDeserializer;

    public CustomDateDeserializer(JsonDeserializer<?> defaultDeserializer) {
        super(Object.class);
        this.defaultDeserializer = defaultDeserializer;
    }

    /**
     * Десериализует строку в дату, если она соответствует указанным форматам, если нет, то вызывается дефолтный десериализатор.
     */
    @Override
    public Object deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        String jsonString = jsonparser.getText();
        if (patterns == null || exclude(jsonparser.getCurrentName()))
            return defaultDeserializer.deserialize(jsonparser, context);
        try {
            return DateUtils.parseDate(jsonString, patterns);
        } catch (ParseException e) {
            return defaultDeserializer.deserialize(jsonparser, context);
        }
    }

    private boolean exclude(String key) {
        return exclusions != null
                && key != null
                && exclusions.contains(key);
    }

    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }

    public void setExclusions(String[] exclusions) {
        this.exclusions = new HashSet<>(Arrays.asList(exclusions));
    }
}
