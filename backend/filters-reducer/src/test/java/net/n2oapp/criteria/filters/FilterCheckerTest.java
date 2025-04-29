package net.n2oapp.criteria.filters;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Test for {@link FilterChecker}
 */
class FilterCheckerTest {


    @Test
    void test() throws Exception {
        //eq
        assert new Filter(1, FilterTypeEnum.eq).check(1);
        assert new Filter("1", FilterTypeEnum.eq).check("1");
        assert new Filter("1", FilterTypeEnum.eq).check(1);
        assert new Filter(1, FilterTypeEnum.eq).check("1");
        assert !new Filter(1, FilterTypeEnum.eq).check(2);
        assert !new Filter(1, FilterTypeEnum.eq).check(null);

        //eqOrIsNull
        assert new Filter(1, FilterTypeEnum.eqOrIsNull).check(1);
        assert !new Filter(1, FilterTypeEnum.eqOrIsNull).check(2);
        assert new Filter(1, FilterTypeEnum.eqOrIsNull).check(null);

        //notEq
        assert !new Filter(1, FilterTypeEnum.notEq).check(1);
        assert new Filter(1, FilterTypeEnum.notEq).check(2);

        //inOrIsNull
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.inOrIsNull).check(1);
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.inOrIsNull).check(3);
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.inOrIsNull).check(null);

        //in
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.in).check(1);
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.in).check("1");
        assert new Filter(Arrays.asList("1", "2"), FilterTypeEnum.in).check(1);
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.in).check(3);
        assert new Filter(Arrays.asList(Arrays.asList(1, 3), Arrays.asList(1, 2), Arrays.asList(2, 3)), FilterTypeEnum.in).check(Arrays.asList(1, 2));
        assert !new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.in).check(Arrays.asList(1, 2));
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.in).check(null);

        //notIn
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.notIn).check(1);
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.notIn).check(3);

        //more
        assert new Filter(1, FilterTypeEnum.more).check(2);
        assert !new Filter(2, FilterTypeEnum.more).check(1);

        //less
        assert !new Filter(1, FilterTypeEnum.less).check(2);
        assert new Filter(2, FilterTypeEnum.less).check(1);

        //isNull
        assert !new Filter(FilterTypeEnum.isNull).check(123);
        assert new Filter(FilterTypeEnum.isNull).check(null);

        //isNotNull
        assert new Filter(FilterTypeEnum.isNotNull).check(123);
        assert !new Filter(FilterTypeEnum.isNotNull).check(null);

        //like
        assert new Filter("test text", FilterTypeEnum.like).check("test");
        assert new Filter("test text", FilterTypeEnum.like).check("est t");
        assert new Filter("test text", FilterTypeEnum.like).check("text");
        assert !new Filter("test text", FilterTypeEnum.like).check("wee");

        //likeStart
        assert new Filter("test text", FilterTypeEnum.likeStart).check("test");
        assert !new Filter("test text", FilterTypeEnum.likeStart).check("est t");
        assert !new Filter("test text", FilterTypeEnum.likeStart).check("text");
        assert !new Filter("test text", FilterTypeEnum.likeStart).check("wee");

        //overlap
        assert new Filter(Arrays.asList(1), FilterTypeEnum.overlaps).check(Arrays.asList(1, 2, 3));
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.overlaps).check(Arrays.asList(2, 3));
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.overlaps).check(Arrays.asList(2, 1));
        assert new Filter(Arrays.asList(1, 2), FilterTypeEnum.overlaps).check(Arrays.asList(1));
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.overlaps).check(Arrays.asList(3, 4));
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.overlaps).check(null);

        //contains
        assert new Filter(Arrays.asList(1), FilterTypeEnum.contains).check(Arrays.asList(1, 2, 3));
        assert new Filter(Arrays.asList(3, 1), FilterTypeEnum.contains).check(Arrays.asList(1, 2, 3));
        assert new Filter(Arrays.asList(3, 1, 2), FilterTypeEnum.contains).check(Arrays.asList(1, 2, 3));
        assert !new Filter(Arrays.asList(1, 5), FilterTypeEnum.contains).check(Arrays.asList(1, 2, 3));
        assert !new Filter(Arrays.asList(1, 2, 3, 4), FilterTypeEnum.contains).check(Arrays.asList(1, 2, 3));
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.contains).check(null);
        assert !new Filter(Arrays.asList(1, 2), FilterTypeEnum.contains).check(new ArrayList<>());
        assert !new Filter(Collections.emptyList(), FilterTypeEnum.contains).check(Arrays.asList(1));
    }
}
