package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.SourceActionsAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.Map;

/**
 * Исходная модель действия "клик по строке таблицы"
 */
@Getter
@Setter
public class N2oRowClick implements SourceActionsAware, Source, ExtensionAttributesAware {
    private String actionId;

    private String enabled;

    private N2oAction[] actions;

    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;
}
