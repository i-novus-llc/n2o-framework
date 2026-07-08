package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.action.N2oAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Исходная модель кнопки
 */
@Getter
@Setter
public class N2oButton extends N2oAbstractButton implements Button {
    private String actionId;
    private N2oAction[] actions;
    private Boolean rounded;
    private Boolean validate;
    private String[] validateDatasourceIds;
    private Dependency[] dependencies;
    private DisableOnEmptyModelTypeEnum disableOnEmptyModel;

    private boolean isGeneratedForSubMenu = false;

    @Override
    public List<N2oAction> getListActions() {
        if (actions == null)
            return new ArrayList<>();
        return Arrays.asList(actions);
    }
}

