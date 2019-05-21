package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;

import java.util.Arrays;
import java.util.List;

/**
 * Исходная модель кнопки
 */
@Getter
@Setter
public class N2oButton extends AbstractMenuItem implements GroupItem {

    private String dropdownSrc;

    @Override
    public List<N2oAction> getActions() {
        return Arrays.asList(getAction());
    }
}

