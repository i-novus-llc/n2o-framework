package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.RegionItem;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.List;
import java.util.Map;

/**
 * Исходная модель региона с отслеживанием прокрутки
 */
@Getter
@Setter
public class N2oScrollspyRegion extends N2oRegion implements RoutableRegion {
    private String title;
    private String active;
    private String placement;
    private Boolean headlines;
    private String maxHeight;
    private Boolean routable;
    private String activeParam;
    private AbstractMenuItem[] menu;

    @Override
    public String getAlias() {
        return "scrollspy";
    }

    @Getter
    @Setter
    public static class MenuItem extends AbstractMenuItem {
        private SourceComponent[] content;

        @Override
        public void collectWidgets(List<N2oWidget> result, Map<String, Integer> ids, String prefix) {
            if (content != null) {
                ids.putIfAbsent(prefix, 1);
                for (SourceComponent component : content) {
                    if (component instanceof RegionItem regionItem)
                        regionItem.collectWidgets(result, ids, prefix);
                }
            }
        }
    }

    @Getter
    @Setter
    public static class SubMenuItem extends AbstractMenuItem {
        private AbstractMenuItem[] subMenu;

        @Override
        public void collectWidgets(List<N2oWidget> result, Map<String, Integer> ids, String prefix) {
            if (subMenu != null) {
                ids.putIfAbsent(prefix, 1);
                for (AbstractMenuItem mi : subMenu) {
                    mi.collectWidgets(result, ids, prefix);
                }
            }
        }
    }

    @Getter
    @Setter
    public static class GroupItem extends AbstractMenuItem {

        private Boolean headline;
        private AbstractMenuItem[] group;

        @Override
        public void collectWidgets(List<N2oWidget> result, Map<String, Integer> ids, String prefix) {
            if (group != null) {
                ids.putIfAbsent(prefix, 1);
                for (AbstractMenuItem mi : group) {
                    mi.collectWidgets(result, ids, prefix);
                }
            }
        }
    }

    @Getter
    @Setter
    public abstract static class AbstractMenuItem implements Source, IdAware, RegionItem {
        private String id;
        private String title;
    }


    @Override
    public void collectWidgets(List<N2oWidget> result, Map<String, Integer> ids, String prefix) {
        if (menu != null) {
            for (AbstractMenuItem mi : menu) {
                mi.collectWidgets(result, ids, getAlias());
            }
        }
    }
}
