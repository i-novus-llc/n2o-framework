package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.NavRegionTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.compile.SecurityExtensionAttributeMapper;
import net.n2oapp.framework.api.metadata.meta.menu.DividerMenuItem;
import net.n2oapp.framework.api.metadata.meta.menu.DropdownMenuItem;
import net.n2oapp.framework.api.metadata.meta.menu.GroupMenuItem;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.NavRegion;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование трансформера доступа региона навигации
 */
class NavRegionTransformerTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack())
                .extensions(new SecurityExtensionAttributeMapper())
                .transformers(new NavRegionTransformer());
    }

    @Test
    void testGroupAndDropdownHideWhenAllChildrenHiddenBySecurity() {
        ReadCompileTerminalPipeline<?> pipeline = compile(
                "net/n2oapp/framework/access/metadata/transform/testNavRegionTransformer.page.xml");
        StandardPage page = (StandardPage) pipeline.transform().get(new PageContext("testNavRegionTransformer"));

        NavRegion navRegion = (NavRegion) page.getRegions().get("single").getFirst();

        // Group "Основная информация": оба menu-item скрыты → group должна скрываться
        GroupMenuItem group1 = (GroupMenuItem) navRegion.getContent().getFirst();
        Security secGroup1 = (Security) group1.getProperties().get(SECURITY_PROP_NAME);
        assertThat(secGroup1, notNullValue());
        assertThat(secGroup1.size(), is(2));
        assertThat(secGroup1.getFirst().get("custom").getRoles(), hasItem("admin"));
        assertThat(secGroup1.get(1).get("custom").getPermissions(), hasItem("edit"));

        // Divider идёт следующим и не влияет на видимость групп
        assertThat(navRegion.getContent().get(1), instanceOf(DividerMenuItem.class));

        // Group "Медицинская информация"
        GroupMenuItem group2 = (GroupMenuItem) navRegion.getContent().get(2);

        // dropdown-menu содержит menu-item "Обязательное" и group "Добровольное" с двумя menu-item
        DropdownMenuItem dropdown = (DropdownMenuItem) group2.getContent().getFirst();

        // group "Добровольное" внутри dropdown: оба menu-item скрыты → group скрывается (size=2: fl, org)
        GroupMenuItem groupDobrovol = (GroupMenuItem) dropdown.getContent().get(1);
        Security secGroupDobrovol = (Security) groupDobrovol.getProperties().get(SECURITY_PROP_NAME);
        assertThat(secGroupDobrovol, notNullValue());
        assertThat(secGroupDobrovol.size(), is(2));
        assertThat(secGroupDobrovol.getFirst().get("custom").getRoles(), hasItem("fl"));
        assertThat(secGroupDobrovol.get(1).get("custom").getRoles(), hasItem("org"));

        // dropdown: все дочерние элементы скрыты → dropdown скрывается (size=3: admin, fl, org)
        Security secDropdown = (Security) dropdown.getProperties().get(SECURITY_PROP_NAME);
        assertThat(secDropdown, notNullValue());
        assertThat(secDropdown.size(), is(3));
        assertThat(secDropdown.getFirst().get("custom").getRoles(), hasItem("admin"));
        assertThat(secDropdown.get(1).get("custom").getRoles(), hasItem("fl"));
        assertThat(secDropdown.get(2).get("custom").getRoles(), hasItem("org"));

        // Group "Медицинская информация": единственный дочерний элемент (dropdown) скрыт → group тоже (size=3)
        Security secGroup2 = (Security) group2.getProperties().get(SECURITY_PROP_NAME);
        assertThat(secGroup2, notNullValue());
        assertThat(secGroup2.size(), is(3));
        assertThat(secGroup2.getFirst().get("custom").getRoles(), hasItem("admin"));
        assertThat(secGroup2.get(1).get("custom").getRoles(), hasItem("fl"));
        assertThat(secGroup2.get(2).get("custom").getRoles(), hasItem("org"));
    }

    @Test
    void testGroupRemainsVisibleWhenAtLeastOneChildHasNoSecurity() {
        ReadCompileTerminalPipeline<?> pipeline = compile(
                "net/n2oapp/framework/access/metadata/transform/testNavRegionTransformerNoMerge.page.xml");
        StandardPage page = (StandardPage) pipeline.transform().get(new PageContext("testNavRegionTransformerNoMerge"));

        NavRegion navRegion = (NavRegion) page.getRegions().get("single").getFirst();

        // Group имеет один скрытый и один всегда видимый menu-item → group не должна скрываться
        GroupMenuItem group = (GroupMenuItem) navRegion.getContent().getFirst();
        Map<String, Object> groupProps = group.getProperties();
        assertThat(groupProps == null || groupProps.get(SECURITY_PROP_NAME) == null, is(true));

        // dropdown-menu имеет один скрытый и один всегда видимый menu-item → dropdown не должна скрываться
        DropdownMenuItem dropdown = (DropdownMenuItem) navRegion.getContent().get(1);
        Map<String, Object> dropdownProps = dropdown.getProperties();
        assertThat(dropdownProps == null || dropdownProps.get(SECURITY_PROP_NAME) == null, is(true));
    }

    @Test
    void testNestedGroupNotPropagated() {
        ReadCompileTerminalPipeline<?> pipeline = compile(
                "net/n2oapp/framework/access/metadata/transform/testNavRegionTransformer.page.xml");
        StandardPage page = (StandardPage) pipeline.transform().get(new PageContext("testNavRegionTransformer"));

        NavRegion navRegion = (NavRegion) page.getRegions().get("single").getFirst();

        // group с вложенной group "Регистрация": трансформер обходит вложенные группы рекурсивно
        GroupMenuItem outerGroup = (GroupMenuItem) navRegion.getContent().get(5);
        // вложенная group "Регистрация" имеет единственный дочерний элемент с security → получает его security
        GroupMenuItem nestedGroup = (GroupMenuItem) outerGroup.getContent().get(2);
        Security secNested = (Security) nestedGroup.getProperties().get(SECURITY_PROP_NAME);
        assertThat(secNested, notNullValue());
        assertThat(secNested.size(), is(1));
        assertThat(secNested.getFirst().get("custom").getRoles(), hasItem("admin2"));
        // внешняя group: все три дочерних элемента имеют security → получает объединённое security всех детей
        Security secOuter = (Security) outerGroup.getProperties().get(SECURITY_PROP_NAME);
        assertThat(secOuter, notNullValue());
        assertThat(secOuter.size(), is(3));
        assertThat(secOuter.getFirst().get("custom").getRoles(), hasItem("admin"));
        assertThat(secOuter.get(1).get("custom").getPermissions(), hasItem("edit"));
        assertThat(secOuter.get(2).get("custom").getRoles(), hasItem("admin2"));
    }

    @Test
    void testNestedDropdownInsideDropdown() {
        ReadCompileTerminalPipeline<?> pipeline = compile(
                "net/n2oapp/framework/access/metadata/transform/testNavRegionTransformer.page.xml");
        StandardPage page = (StandardPage) pipeline.transform().get(new PageContext("testNavRegionTransformer"));

        NavRegion navRegion = (NavRegion) page.getRegions().get("single").getFirst();

        DropdownMenuItem outerDropdown = (DropdownMenuItem) navRegion.getContent().get(6);

        // вложенный dropdown: оба menu-item скрыты → скрывается (size=2: fl, org)
        DropdownMenuItem innerDropdown = (DropdownMenuItem) outerDropdown.getContent().get(1);
        Security secInner = (Security) innerDropdown.getProperties().get(SECURITY_PROP_NAME);
        assertThat(secInner, notNullValue());
        assertThat(secInner.size(), is(2));
        assertThat(secInner.getFirst().get("custom").getRoles(), hasItem("fl"));
        assertThat(secInner.get(1).get("custom").getRoles(), hasItem("org"));

        // внешний dropdown: все дочерние скрыты → скрывается (size=3: admin, fl, org)
        Security secOuter = (Security) outerDropdown.getProperties().get(SECURITY_PROP_NAME);
        assertThat(secOuter, notNullValue());
        assertThat(secOuter.size(), is(3));
        assertThat(secOuter.getFirst().get("custom").getRoles(), hasItem("admin"));
        assertThat(secOuter.get(1).get("custom").getRoles(), hasItem("fl"));
        assertThat(secOuter.get(2).get("custom").getRoles(), hasItem("org"));
    }
}
