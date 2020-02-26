package net.n2oapp.framework.autotest.header;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.compile.ExtensionAttributeMapper;

import java.util.HashMap;
import java.util.Map;

public class TestExtAttributeMapper implements ExtensionAttributeMapper {
    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/test";
    }

    @Override
    public Map<String, Object> mapAttributes(Map<String, String> attributes) {
        Map<String, Object> res = new HashMap<>();
        ExtObject extObject = new ExtObject();
        extObject.setAttr1(attributes.get("attr1"));
        extObject.setAttr2(attributes.get("attr2"));
        extObject.setAttr3(attributes.get("attr3"));
        res.put("extObject", extObject);
        return res;
    }

    @Getter
    @Setter
    public static class ExtObject {
        @JsonProperty
        private String attr1;
        @JsonProperty
        private String attr2;
        @JsonProperty
        private String attr3;
    }
}
