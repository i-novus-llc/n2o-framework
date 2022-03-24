package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil.collectWidgets;

/**
 * Модель страницы с правыми и левыми регионами (left-right-page)
 */
@Getter
@Setter
public class N2oLeftRightPage extends N2oBasePage {
    private SourceComponent[] left;
    private String leftWidth;
    private SourceComponent[] right;
    private String rightWidth;

    @Override
    public List<N2oWidget> getWidgets() {
        List<SourceComponent> sourceComponents = new ArrayList<>();

        if (left != null)
            sourceComponents.addAll(Arrays.asList(left));
        if (right != null)
            sourceComponents.addAll(Arrays.asList(right));

        return collectWidgets(sourceComponents.toArray(new SourceComponent[0]));
    }
}
