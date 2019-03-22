package net.n2oapp.framework.engine.data.rest.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.lang.time.DateUtils;

import java.io.IOException;
import java.text.ParseException;

/**
 * Десериализиует даты, указанных форматов
 */
public class CustomDateDeserializer extends StdDeserializer<Object> {

    private String[] patterns;

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
        if (patterns == null)
            return defaultDeserializer.deserialize(jsonparser, context);
        try {
            return DateUtils.parseDate(jsonString, patterns);
        } catch (ParseException e) {
            return defaultDeserializer.deserialize(jsonparser, context);
        }
    }

    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }
}
