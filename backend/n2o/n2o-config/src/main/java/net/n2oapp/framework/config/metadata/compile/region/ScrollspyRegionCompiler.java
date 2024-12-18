package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oScrollspyRegion;
import net.n2oapp.framework.api.metadata.global.view.region.N2oScrollspyRegion.AbstractMenuItem;
import net.n2oapp.framework.api.metadata.global.view.region.N2oScrollspyRegion.GroupItem;
import net.n2oapp.framework.api.metadata.global.view.region.N2oScrollspyRegion.MenuItem;
import net.n2oapp.framework.api.metadata.global.view.region.N2oScrollspyRegion.SubMenuItem;
import net.n2oapp.framework.api.metadata.meta.region.scrollspy.*;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция региона с отслеживанием прокрутки
 */
@Component
public class ScrollspyRegionCompiler extends BaseRegionCompiler<ScrollspyRegion, N2oScrollspyRegion> {

    private static final String PLACEMENT_PROPERTY = "n2o.api.region.scrollspy.placement";
    private static final String HEADLINES_PROPERTY = "n2o.api.region.scrollspy.headlines";
    private static final String ROUTABLE_PROPERTY = "n2o.api.region.scrollspy.routable";
    private static final String GROUP_HEADLINE_PROPERTY = "n2o.api.region.scrollspy.group.headline";

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oScrollspyRegion.class;
    }

    @Override
    protected String createId(CompileProcessor p) {
        return createId("scrollspy", p);
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.region.scrollspy.src";
    }

    @Override
    public ScrollspyRegion compile(N2oScrollspyRegion source, PageContext context, CompileProcessor p) {
        ScrollspyRegion region = new ScrollspyRegion();
        build(region, source, p);
        region.setTitle(source.getTitle());
        region.setPlacement(
                castDefault(source.getPlacement(), () -> p.resolve(property(PLACEMENT_PROPERTY), String.class))
        );
        region.setHeadlines(
                castDefault(source.getHeadlines(), () -> p.resolve(property(HEADLINES_PROPERTY), Boolean.class))
        );
        region.setMaxHeight(prepareSizeAttribute(source.getMaxHeight()));
        region.setMenu(initMenu(source.getMenu(), context, p));
        region.setActive(source.getActive());
        region.setActiveParam(source.getActiveParam());
        compileRoute(source, region.getId(), ROUTABLE_PROPERTY, p);

        return region;
    }

    private List<ScrollspyElement> initMenu(AbstractMenuItem[] items, PageContext context, CompileProcessor p) {
        if (Objects.isNull(items) || items.length == 0)
            return null;
        List<ScrollspyElement> elements = new ArrayList<>();
        for (AbstractMenuItem item : items) {
            if (item instanceof MenuItem) {
                elements.add(initSingleElement(item, context, p));
            } else if (item instanceof SubMenuItem) {
                elements.add(initSubElement(item, context, p));
            } else if (item instanceof GroupItem) {
                elements.add(initGroupElement(item, context, p));
            }
        }

        return elements;
    }

    private SingleScrollspyElement initSingleElement(AbstractMenuItem item, PageContext context, CompileProcessor p) {
        SingleScrollspyElement element = new SingleScrollspyElement();
        initElement(element, item, p);
        element.setContent(initContent(((MenuItem) item).getContent(), context, p, item));

        return element;
    }

    private MenuScrollspyElement initSubElement(AbstractMenuItem item, PageContext context, CompileProcessor p) {
        MenuScrollspyElement element = new MenuScrollspyElement();
        initElement(element, item, p);
        element.setMenu(initMenu(((SubMenuItem) item).getSubMenu(), context, p));

        return element;
    }

    private GroupScrollspyElement initGroupElement(AbstractMenuItem item, PageContext context, CompileProcessor p) {
        GroupScrollspyElement element = new GroupScrollspyElement();
        initElement(element, item, p);
        element.setGroup(initMenu(((GroupItem) item).getGroup(), context, p));
        element.setHeadline(
                castDefault(((GroupItem) item).getHeadline(), () -> p.resolve(property(GROUP_HEADLINE_PROPERTY), Boolean.class))
        );

        return element;
    }

    private void initElement(ScrollspyElement element, AbstractMenuItem item, CompileProcessor p) {
        element.setId(castDefault(item.getId(), createId("element_scrollspy", p)));
        element.setTitle(item.getTitle());
    }
}
