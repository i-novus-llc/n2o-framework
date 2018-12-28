package net.n2oapp.criteria.filters;

import org.junit.Test;

import java.util.Arrays;

/**
 * User: operehod
 * Date: 05.12.2014
 * Time: 19:04
 */
public class FilterCheckerTest {


    @Test
    public void test() throws Exception {
        //eq
        assert new Filter(1, FilterType.eq).check(1);
        assert !new Filter(1, FilterType.eq).check(2);
        assert !new Filter(1, FilterType.eq).check(null);

        //eqOrIsNull
        assert new Filter(1, FilterType.eqOrIsNull).check(1);
        assert !new Filter(1, FilterType.eqOrIsNull).check(2);
        assert new Filter(1, FilterType.eqOrIsNull).check(null);

        //notEq
        assert !new Filter(1, FilterType.notEq).check(1);
        assert new Filter(1, FilterType.notEq).check(2);

        //inOrIsNull
        assert new Filter(Arrays.asList(1, 2), FilterType.inOrIsNull).check(1);
        assert !new Filter(Arrays.asList(1, 2), FilterType.inOrIsNull).check(3);
        assert new Filter(Arrays.asList(1, 2), FilterType.inOrIsNull).check(null);

        //in
        assert new Filter(Arrays.asList(1, 2), FilterType.in).check(1);
        assert !new Filter(Arrays.asList(1, 2), FilterType.in).check(3);
        assert new Filter(Arrays.asList(1, 2, 3), FilterType.in).check(Arrays.asList(1, 2));
        assert !new Filter(Arrays.asList(1, 2, 3), FilterType.in).check(Arrays.asList(1, 4));
        assert !new Filter(Arrays.asList(1, 2), FilterType.in).check(null);

        //notIn
        assert !new Filter(Arrays.asList(1, 2), FilterType.notIn).check(1);
        assert new Filter(Arrays.asList(1, 2), FilterType.notIn).check(3);

        //more
        assert new Filter(1, FilterType.more).check(2);
        assert !new Filter(2, FilterType.more).check(1);

        //less
        assert !new Filter(1, FilterType.less).check(2);
        assert new Filter(2, FilterType.less).check(1);

        //isNull
        assert !new Filter(FilterType.isNull).check(123);
        assert new Filter(FilterType.isNull).check(null);

        //isNotNull
        assert new Filter(FilterType.isNotNull).check(123);
        assert !new Filter(FilterType.isNotNull).check(null);

        //like
        assert new Filter("test text", FilterType.like).check("test");
        assert new Filter("test text", FilterType.like).check("est t");
        assert new Filter("test text", FilterType.like).check("text");
        assert !new Filter("test text", FilterType.like).check("wee");

        //likeStart
        assert new Filter("test text", FilterType.likeStart).check("test");
        assert !new Filter("test text", FilterType.likeStart).check("est t");
        assert !new Filter("test text", FilterType.likeStart).check("text");
        assert !new Filter("test text", FilterType.likeStart).check("wee");

        //overlap
        assert new Filter(Arrays.asList(1, 2, 3), FilterType.overlaps).check(Arrays.asList(1));
        assert new Filter(Arrays.asList(1, 2, 3), FilterType.overlaps).check(Arrays.asList(2, 5));
        assert !new Filter(Arrays.asList(1, 2, 3), FilterType.overlaps).check(Arrays.asList(5));
        assert !new Filter(Arrays.asList(1, 2), FilterType.overlaps).check(null);

        //contains
        assert new Filter(Arrays.asList(1, 2, 3), FilterType.contains).check(Arrays.asList(1));
        assert new Filter(Arrays.asList(1, 2, 3), FilterType.contains).check(Arrays.asList(2, 3));
        assert !new Filter(Arrays.asList(1, 2, 3), FilterType.contains).check(Arrays.asList(1, 5));
        assert !new Filter(Arrays.asList(1, 2, 3), FilterType.contains).check(Arrays.asList(4, 5));
        assert !new Filter(Arrays.asList(1, 2), FilterType.contains).check(null);
    }
}
