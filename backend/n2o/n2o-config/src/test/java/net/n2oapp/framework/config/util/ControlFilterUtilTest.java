package net.n2oapp.framework.config.util;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static net.n2oapp.framework.config.util.ControlFilterUtil.getFilters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ControlFilterUtilTest {

    @Test
    public void testGetFilters() {
        CompiledQuery query = new CompiledQuery();
        Map<String, N2oQuery.Filter> filterMap = new HashMap<>();
        filterMap.put("name", new N2oQuery.Filter("name", FilterType.eq));
        filterMap.put("nameLike", new N2oQuery.Filter("nameLike", FilterType.eq));
        filterMap.put("gender", new N2oQuery.Filter("gender", FilterType.eq));
        filterMap.put("gender.id", new N2oQuery.Filter("gender.id", FilterType.eq));
        filterMap.put("gender.id.test", new N2oQuery.Filter("gender.id.test", FilterType.eq));
        filterMap.put("gender.id.test1.test2", new N2oQuery.Filter("gender.id.test1.test2", FilterType.eq));
        filterMap.put("test1.test2", new N2oQuery.Filter("test1.test2", FilterType.eq));
        filterMap.put("gender.name", new N2oQuery.Filter("gender.name", FilterType.eq));
        filterMap.put("parent*.id", new N2oQuery.Filter("parent*.id", FilterType.in));
        filterMap.put("parent*.name", new N2oQuery.Filter("parent*.name", FilterType.in));
        filterMap.put("parent*.name.like", new N2oQuery.Filter("parent*.name.like", FilterType.in));
        filterMap.put("parent.name*.like", new N2oQuery.Filter("parent.name*.like", FilterType.in));
        filterMap.put("parent.id*.like", new N2oQuery.Filter("parent.id*.like", FilterType.in));
        filterMap.put("parent.id*.like.like2", new N2oQuery.Filter("parent.id*.like.like2", FilterType.in));


        query.setFilterFieldsMap(filterMap);

        assertThat(getFilters("name", query).size(), is(1));

        List<N2oQuery.Filter> filters = getFilters("gender", query);
        Set<String> filterIds = filters.stream().map(f -> f.getFilterId()).collect(Collectors.toSet());
        assertThat(filters.size(), is(3));
        assertThat(filterIds.contains("gender"), is(true));
        assertThat(filterIds.contains("gender.id"), is(true));
        assertThat(filterIds.contains("gender.name"), is(true));

        filters = getFilters("gender.id", query);
        filterIds = filters.stream().map(f -> f.getFilterId()).collect(Collectors.toSet());
        assertThat(filters.size(), is(2));
        assertThat(filterIds.contains("gender.id"), is(true));
        assertThat(filterIds.contains("gender.id.test"), is(true));

        filters = getFilters("parent", query);
        filterIds = filters.stream().map(f -> f.getFilterId()).collect(Collectors.toSet());
        assertThat(filters.size(), is(2));
        assertThat(filterIds.contains("parent*.id"), is(true));
        assertThat(filterIds.contains("parent*.name"), is(true));

        filters = getFilters("parent.id", query);
        filterIds = filters.stream().map(f -> f.getFilterId()).collect(Collectors.toSet());
        assertThat(filters.size(), is(1));
        assertThat(filterIds.contains("parent.id*.like"), is(true));
    }
}
