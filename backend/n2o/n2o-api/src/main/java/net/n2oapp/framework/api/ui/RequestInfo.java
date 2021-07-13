package net.n2oapp.framework.api.ui;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;
import net.n2oapp.framework.api.user.UserContext;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Информация о запросе
 */
@Getter
@Setter
public class RequestInfo {
    private UserContext user;
    private Map<String, Object> attributes;
    private DataSet queryData;
    private String failAlertWidgetId;
    private String successAlertWidgetId;
    private String messagesForm;
    private MessagePosition messagePosition;
    private MessagePlacement messagePlacement;
    private CompileContext<?, ?> context;

    public void addAttribute(String name, Object value) {
        if (attributes == null) {
            attributes = new LinkedHashMap<>();
        }
        attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        if (attributes == null) {
            return null;
        }
        return attributes.get(name);
    }
}
