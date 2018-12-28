package net.n2oapp.framework.api.function;

import net.n2oapp.criteria.dataset.Interval;
import net.n2oapp.framework.api.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FunctionProcessor {

    public static final String SPEC_SYMBOL_FOR_CLIENT = "`";

    public static FunctionProcessor getInstance() {
        return Holder.instance;
    }

    public String resolve(String value) {
        return resolveFunctions(value);
    }

    public Interval resolveInterval(Interval value) {
        if (value.getBegin() == null && value.getEnd() == null)
            return value;
        if ((value.getBegin() == null || value.getBegin() instanceof String)
                && (value.getEnd() == null || value.getEnd() instanceof String)) {
            Interval interval = new Interval(
                    value.getBegin() != null ? resolveFunctions((String) value.getBegin()) : value.getBegin(),
                    value.getEnd() != null ? resolveFunctions((String) value.getEnd()) : value.getEnd());
            return interval;
        }
        return value;
    }

    public Map<String, Object> resolveMap(Map<String, Object> value) {
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            if (entry.getValue() instanceof String)
                entry.setValue(resolveFunctions((String) entry.getValue()));
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public List<Object> resolveList(List<Object> value) {
        List<Object> result = new ArrayList<>();
        for (Object item : value) {
            if (item instanceof Map)
                result.add(resolveMap((Map<String, Object>) item));
            else
                result.add(item);
        }
        return result;
    }

    private String resolveFunctions(String value) {
        if (StringUtils.isFunction(value))
            return SPEC_SYMBOL_FOR_CLIENT + value + SPEC_SYMBOL_FOR_CLIENT;
        return value;
    }

    private static class Holder {
        private static FunctionProcessor instance = new FunctionProcessor();
    }
}
