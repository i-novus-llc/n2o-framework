package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import static net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil.collectWidgets;

/**
 * Исходная модель страницы с тремя регионами
 */
@Getter
@Setter
public class N2oTopLeftRightPage extends N2oBasePage {
    private Boolean scrollTopButton;
    private SourceComponent[] top;
    private RegionOptions topOptions = new RegionOptions();
    private SourceComponent[] left;
    private RegionOptions leftOptions = new RegionOptions();
    private SourceComponent[] right;
    private RegionOptions rightOptions = new RegionOptions();

    @Getter
    @Setter
    public static class RegionOptions implements Source {
        private String width;
        private Boolean fixed;
        private Integer offset;
    }

    @Override
    public List<N2oWidget> getWidgets() {
        List<N2oWidget> containers = new ArrayList<>();
        Map<String, Integer> ids = new HashMap<>();
        List<SourceComponent> sourceComponents = new ArrayList<>();

        if (top != null)
            sourceComponents.addAll(Arrays.asList(top));
        if (left != null)
            sourceComponents.addAll(Arrays.asList(left));
        if (right != null)
            sourceComponents.addAll(Arrays.asList(right));

        containers.addAll(collectWidgets(sourceComponents.toArray(new SourceComponent[0]), ids));
        return containers;
    }
}
