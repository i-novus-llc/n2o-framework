package net.n2oapp.framework.api.filters;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterChecker;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test for {@link FilterChecker}
 */
class FilterCheckerTest {


    @Test
    void test() throws Exception {
        //eq
        assert new Filter(1, FilterTypeEnum.EQ).check(1);
        assert new Filter("1", FilterTypeEnum.EQ).check("1");
        assert new Filter("1", FilterTypeEnum.EQ).check(1);
        assert new Filter(1, FilterTypeEnum.EQ).check("1");
        assert !new Filter(1, FilterTypeEnum.EQ).check(2);
        assert !new Filter(1, FilterTypeEnum.EQ).check(null);

        //eqOrIsNull
        assert new Filter(1, FilterTypeEnum.EQ_OR_IS_NULL).check(1);
        assert !new Filter(1, FilterTypeEnum.EQ_OR_IS_NULL).check(2);
        assert new Filter(1, FilterTypeEnum.EQ_OR_IS_NULL).check(null);

        //notEq
        assert !new Filter(1, FilterTypeEnum.NOT_EQ).check(1);
        assert new Filter(1, FilterTypeEnum.NOT_EQ).check(2);

        //inOrIsNull
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.IN_OR_IS_NULL).check(1);
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.IN_OR_IS_NULL).check(3);
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.IN_OR_IS_NULL).check(null);

        //in
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.IN).check(1);
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.IN).check("1");
        assert new Filter(Arrays.asList("1", "2"), FilterTypeEnum.IN).check(1);
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.IN).check(3);
        assert new Filter(Arrays.asList(Arrays.asList(1, 3), Arrays.asList(1, 2), Arrays.asList(2, 3)), FilterTypeEnum.IN).check(Arrays.asList(1, 2));
        assert !new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.IN).check(Arrays.asList(1, 2));
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.IN).check(null);

        //notIn
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.NOT_IN).check(1);
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.NOT_IN).check(3);

        //more
        assert new Filter(1, FilterTypeEnum.MORE).check(2);
        assert !new Filter(2, FilterTypeEnum.MORE).check(1);

        //less
        assert !new Filter(1, FilterTypeEnum.LESS).check(2);
        assert new Filter(2, FilterTypeEnum.LESS).check(1);

        //isNull
        assert !new Filter(FilterTypeEnum.IS_NULL).check(123);
        assert new Filter(FilterTypeEnum.IS_NULL).check(null);

        //isNotNull
        assert new Filter(FilterTypeEnum.IS_NOT_NULL).check(123);
        assert !new Filter(FilterTypeEnum.IS_NOT_NULL).check(null);

        //like
        assert new Filter("test text", FilterTypeEnum.LIKE).check("test");
        assert new Filter("test text", FilterTypeEnum.LIKE).check("est t");
        assert new Filter("test text", FilterTypeEnum.LIKE).check("text");
        assert !new Filter("test text", FilterTypeEnum.LIKE).check("wee");

        //likeStart
        assert new Filter("test text", FilterTypeEnum.LIKE_START).check("test");
        assert !new Filter("test text", FilterTypeEnum.LIKE_START).check("est t");
        assert !new Filter("test text", FilterTypeEnum.LIKE_START).check("text");
        assert !new Filter("test text", FilterTypeEnum.LIKE_START).check("wee");

        //overlap
        assert new Filter(List.of(1), FilterTypeEnum.OVERLAPS).check(Arrays.asList(1, 2, 3));
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.OVERLAPS).check(Arrays.asList(2, 3));
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.OVERLAPS).check(Arrays.asList(2, 1));
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.OVERLAPS).check(List.of(1));
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.OVERLAPS).check(Arrays.asList(3, 4));
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.OVERLAPS).check(null);

        //contains
        assert new Filter(List.of(1), FilterTypeEnum.CONTAINS).check(Arrays.asList(1, 2, 3));
        assert new Filter(Arrays.asList(3, 1), FilterTypeEnum.CONTAINS).check(Arrays.asList(1, 2, 3));
        assert new Filter(Arrays.asList(3, 1, 2), FilterTypeEnum.CONTAINS).check(Arrays.asList(1, 2, 3));
        assert !new Filter(Arrays.asList(1, 5), FilterTypeEnum.CONTAINS).check(Arrays.asList(1, 2, 3));
        assert !new Filter(Arrays.asList(1, 2, 3, 4), FilterTypeEnum.CONTAINS).check(Arrays.asList(1, 2, 3));
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.CONTAINS).check(null);
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.CONTAINS).check(new ArrayList<>());
        assert !new Filter(Collections.emptyList(), FilterTypeEnum.CONTAINS).check(List.of(1));
    }
}
