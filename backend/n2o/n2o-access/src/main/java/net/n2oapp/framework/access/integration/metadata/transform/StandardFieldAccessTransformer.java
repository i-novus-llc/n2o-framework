package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Трансформатор доступа StandardField
 */
@Component
public class StandardFieldAccessTransformer extends BaseAccessTransformer<StandardField, CompileContext<?, ?>> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return StandardField.class;
    }

    @Override
    public StandardField transform(StandardField compiled, CompileContext<?, ?> context, CompileProcessor p) {
        if (compiled.getControl().getProperties() != null) {
            Map<String, Object> secProps = compiled.getProperties() == null ? new HashMap<>() : compiled.getProperties();
            Iterator<Map.Entry<String, Object>> iter = compiled.getControl().getProperties().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> prop = iter.next();
                if (prop.getKey().equals(Security.SECURITY_PROP_NAME)) {
                    iter.remove();
                    secProps.put(prop.getKey(), prop.getValue());
                }
            }
            if (!secProps.isEmpty()) compiled.setProperties(secProps);
        }
        return compiled;
    }
}
