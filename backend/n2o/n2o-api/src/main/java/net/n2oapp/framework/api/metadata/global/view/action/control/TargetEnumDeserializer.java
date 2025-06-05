package net.n2oapp.framework.api.metadata.global.view.action.control;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class TargetEnumDeserializer extends StdDeserializer<TargetEnum> {
    public TargetEnumDeserializer() {
        super(TargetEnum.class);
    }

    @Override
    public TargetEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        for (TargetEnum e : TargetEnum.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
