package net.n2oapp.framework.config.util;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static net.n2oapp.framework.config.util.FieldCompileUtil.getFilters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class FieldCompileUtilTest {

    @Test
    void testGetFilters() {
        CompiledQuery query = new CompiledQuery();
        Map<String, N2oQuery.Filter> filterMap = new HashMap<>();
        filterMap.put("name", new N2oQuery.Filter("name", FilterTypeEnum.eq));
        filterMap.put("nameLike", new N2oQuery.Filter("nameLike", FilterTypeEnum.eq));
        filterMap.put("gender", new N2oQuery.Filter("gender", FilterTypeEnum.eq));
        filterMap.put("gender.id", new N2oQuery.Filter("gender.id", FilterTypeEnum.eq));
        filterMap.put("gender.id.test", new N2oQuery.Filter("gender.id.test", FilterTypeEnum.eq));
        filterMap.put("gender.id.test1.test2", new N2oQuery.Filter("gender.id.test1.test2", FilterTypeEnum.eq));
        filterMap.put("test1.test2", new N2oQuery.Filter("test1.test2", FilterTypeEnum.eq));
        filterMap.put("gender.name", new N2oQuery.Filter("gender.name", FilterTypeEnum.eq));
        filterMap.put("parent*.id", new N2oQuery.Filter("parent*.id", FilterTypeEnum.in));
        filterMap.put("parent*.name", new N2oQuery.Filter("parent*.name", FilterTypeEnum.in));
        filterMap.put("parent*.name.like", new N2oQuery.Filter("parent*.name.like", FilterTypeEnum.in));
        filterMap.put("parent.name*.like", new N2oQuery.Filter("parent.name*.like", FilterTypeEnum.in));
        filterMap.put("parent.id*.like", new N2oQuery.Filter("parent.id*.like", FilterTypeEnum.in));
        filterMap.put("parent.id*.like.like2", new N2oQuery.Filter("parent.id*.like.like2", FilterTypeEnum.in));


        query.setFilterFieldsMap(filterMap);

        assertThat(getFilters("name", query).size(), is(1));

        List<N2oQuery.Filter> filters = getFilters("gender", query);
        Set<String> filterIds = filters.stream().map(N2oQuery.Filter::getFilterId).collect(Collectors.toSet());
        assertThat(filters.size(), is(3));
        assertThat(filterIds.contains("gender"), is(true));
        assertThat(filterIds.contains("gender.id"), is(true));
        assertThat(filterIds.contains("gender.name"), is(true));

        filters = getFilters("gender.id", query);
        filterIds = filters.stream().map(N2oQuery.Filter::getFilterId).collect(Collectors.toSet());
        assertThat(filters.size(), is(2));
        assertThat(filterIds.contains("gender.id"), is(true));
        assertThat(filterIds.contains("gender.id.test"), is(true));

        filters = getFilters("parent", query);
        filterIds = filters.stream().map(N2oQuery.Filter::getFilterId).collect(Collectors.toSet());
        assertThat(filters.size(), is(2));
        assertThat(filterIds.contains("parent*.id"), is(true));
        assertThat(filterIds.contains("parent*.name"), is(true));

        filters = getFilters("parent.id", query);
        filterIds = filters.stream().map(N2oQuery.Filter::getFilterId).collect(Collectors.toSet());
        assertThat(filters.size(), is(1));
        assertThat(filterIds.contains("parent.id*.like"), is(true));
    }
}
