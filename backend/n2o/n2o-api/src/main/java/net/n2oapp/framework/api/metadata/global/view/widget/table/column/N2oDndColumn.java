package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.Map;

/**
 * Исходная модель drag-n-drop столбца таблицы
 */
@Getter
@Setter
public class N2oDndColumn extends N2oAbstractColumn implements ExtensionAttributesAware {
    private MoveModeEnum moveMode;
    private N2oAbstractColumn[] children;
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;
}
