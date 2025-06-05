package net.n2oapp.framework.api.metadata.global.view.action.control;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class TargetEnumSerializer extends StdSerializer<TargetEnum> {
    public TargetEnumSerializer() {
        super(TargetEnum.class);
    }

    @Override
    public void serialize(TargetEnum value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getValue());
    }
}
