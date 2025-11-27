package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.region.N2oFlexRowRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil.collectWidgets;

/**
 * @deprecated
 * Модель страницы с правыми и левыми регионами (left-right-page)
 */
@Deprecated(since = "7.29")
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

    /**
     * Преобразование <left> и <right> в <flex-row>
     * Если ширина задана, то обернуть содержимое в <region>, которому задать стили.
     */
    public void adapterV4() {
        if (left == null && right == null)
            return;

        String[] calculatedWidths = calculateWidths(leftWidth, rightWidth);

        List<SourceComponent> regionContent = new ArrayList<>();

        addContent(regionContent, left, calculatedWidths[0]);
        addContent(regionContent, right, calculatedWidths[1]);

        N2oFlexRowRegion flexRowRegion = new N2oFlexRowRegion();
        flexRowRegion.setContent(regionContent.toArray(new SourceComponent[0]));

        setItems(new SourceComponent[]{flexRowRegion});
    }
}