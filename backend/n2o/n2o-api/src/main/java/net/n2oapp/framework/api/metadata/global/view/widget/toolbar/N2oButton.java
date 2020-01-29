package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SrcAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;

import java.util.Arrays;
import java.util.List;

/**
 * Исходная модель кнопки
 */
@Getter
@Setter
public class N2oButton extends AbstractMenuItem implements SrcAware, GroupItem, Source {

    private String src;

    @Override
    public List<N2oAction> getActions() {
        return Arrays.asList(getAction());
    }
}

