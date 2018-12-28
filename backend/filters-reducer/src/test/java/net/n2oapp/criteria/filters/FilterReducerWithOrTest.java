package net.n2oapp.criteria.filters;

import org.junit.Test;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static net.n2oapp.criteria.filters.FilterReducer.reduce;

/**
 * User: operehod
 * Date: 19.11.2014
 * Time: 11:15
 */
public class FilterReducerWithOrTest {


    @Test
    public void testEqOrIsNull() throws Exception {
        //eq
        Result result = reduce(
                new Filter(1, FilterType.eqOrIsNull),
                new Filter(1, FilterType.eq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);
        result = reduce(
                new Filter(1, FilterType.eq),
                new Filter(1, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);
        result = reduce(
                new Filter(2, FilterType.eq),
                new Filter(1, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.conflict);

        //eqOrIsNull
        result = reduce(
                new Filter(1, FilterType.eqOrIsNull),
                new Filter(1, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(1);
        result = reduce(
                new Filter(2, FilterType.eqOrIsNull),
                new Filter(1, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);

        //notEq
        result = reduce(
                new Filter(1, FilterType.eqOrIsNull),
                new Filter(1, FilterType.notEq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);
        result = reduce(
                new Filter(1, FilterType.notEq),
                new Filter(1, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);
        result = reduce(
                new Filter(1, FilterType.notEq),
                new Filter(2, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(2);

        //more
        result = reduce(
                new Filter(4, FilterType.eqOrIsNull),
                new Filter(1, FilterType.more));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(1, FilterType.more),
                new Filter(4, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(4, FilterType.more),
                new Filter(4, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.conflict);

        //less
        result = reduce(
                new Filter(4, FilterType.eqOrIsNull),
                new Filter(7, FilterType.less));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(7, FilterType.less),
                new Filter(4, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(4, FilterType.less),
                new Filter(4, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.conflict);

        //isNull
        result = reduce(
                new Filter(4, FilterType.eqOrIsNull),
                new Filter(FilterType.isNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);
        result = reduce(
                new Filter(FilterType.isNull),
                new Filter(4, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);

        //isNotNull
        result = reduce(
                new Filter(4, FilterType.eqOrIsNull),
                new Filter(FilterType.isNotNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(FilterType.isNotNull),
                new Filter(4, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(4);

        //in
        result = reduce(
                new Filter(4, FilterType.eqOrIsNull),
                new Filter(asList(4, 5, 6), FilterType.in));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(asList(4, 5, 6), FilterType.in),
                new Filter(4, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(asList(7, 8, 9), FilterType.in),
                new Filter(4, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.conflict);

        //inOrIsNull
        result = reduce(
                new Filter(4, FilterType.eqOrIsNull),
                new Filter(asList(4, 5, 6), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(asList(4, 5, 6), FilterType.inOrIsNull),
                new Filter(4, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(asList(7, 8, 9), FilterType.inOrIsNull),
                new Filter(4, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);

        //notIn
        result = reduce(
                new Filter(4, FilterType.eqOrIsNull),
                new Filter(asList(5, 6), FilterType.notIn));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(asList(5, 6), FilterType.notIn),
                new Filter(4, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(asList(4, 5), FilterType.notIn),
                new Filter(4, FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);

        //like
        result = reduce(
                new Filter("es", FilterType.like),
                new Filter("test", FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals("test");
        result = reduce(
                new Filter("esy", FilterType.like),
                new Filter("test", FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.conflict);

        //likeStart
        result = reduce(
                new Filter("tes", FilterType.likeStart),
                new Filter("test", FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals("test");
        result = reduce(
                new Filter("es", FilterType.likeStart),
                new Filter("test", FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.conflict);

        //overlap
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterType.overlaps),
                new Filter(Arrays.asList(1, 2, 4), FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 2, 4));
        result = reduce(
                new Filter(Arrays.asList(6, 5), FilterType.overlaps),
                new Filter(Arrays.asList(1, 2), FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.conflict);

        //contains
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterType.contains),
                new Filter(Arrays.asList(1, 2, 4), FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 2, 4));
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterType.contains),
                new Filter(Arrays.asList(5, 2), FilterType.eqOrIsNull));
        assert result.getType().equals(Result.Type.conflict);
    }


    @Test
    public void testInOrIsNull() throws Exception {
        //eq
        Result result = reduce(
                new Filter(asList(1, 2), FilterType.inOrIsNull),
                new Filter(1, FilterType.eq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);
        result = reduce(
                new Filter(1, FilterType.eq),
                new Filter(asList(1, 2), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);
        result = reduce(
                new Filter(3, FilterType.eq),
                new Filter(asList(1, 2), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.conflict);


        //notEq
        result = reduce(
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull),
                new Filter(2, FilterType.notEq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.inOrIsNull);
        assert result.getResultFilter().getValue().equals(asList(1, 3));
        result = reduce(
                new Filter(2, FilterType.notEq),
                new Filter(asList(1, 2), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(1);
        result = reduce(
                new Filter(1, FilterType.notEq),
                new Filter(asList(1), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);
        //more
        result = reduce(
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull),
                new Filter(2, FilterType.more));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(3);
        result = reduce(
                new Filter(2, FilterType.more),
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(3);
        result = reduce(
                new Filter(1, FilterType.more),
                new Filter(asList(1), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.conflict);

        //less
        result = reduce(
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull),
                new Filter(2, FilterType.less));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);
        result = reduce(
                new Filter(2, FilterType.less),
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);
        result = reduce(
                new Filter(1, FilterType.less),
                new Filter(asList(1), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.conflict);

        //isNull
        result = reduce(
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull),
                new Filter(FilterType.isNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);
        result = reduce(
                new Filter(FilterType.isNull),
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);


        //isNotNull
        result = reduce(
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull),
                new Filter(FilterType.isNotNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.in);
        assert result.getResultFilter().getValue().equals(asList(1, 2, 3));
        result = reduce(
                new Filter(FilterType.isNotNull),
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.in);
        assert result.getResultFilter().getValue().equals(asList(1, 2, 3));

        //in
        result = reduce(
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull),
                new Filter(asList(2, 3, 4), FilterType.in));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.in);
        assert result.getResultFilter().getValue().equals(asList(2, 3));
        result = reduce(
                new Filter(asList(3, 4), FilterType.in),
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(3);
        result = reduce(
                new Filter(asList(2), FilterType.in),
                new Filter(asList(1), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.conflict);

        //inOrIsNull
        result = reduce(
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull),
                new Filter(asList(2, 3, 4), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.inOrIsNull);
        assert result.getResultFilter().getValue().equals(asList(2, 3));
        result = reduce(
                new Filter(asList(3, 4), FilterType.inOrIsNull),
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(3);
        result = reduce(
                new Filter(asList(2), FilterType.inOrIsNull),
                new Filter(asList(1), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);

        //notIn
        result = reduce(
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull),
                new Filter(asList(2, 3, 4), FilterType.notIn));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(1);
        result = reduce(
                new Filter(asList(3, 4), FilterType.notIn),
                new Filter(asList(1, 2, 3), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.inOrIsNull);
        assert result.getResultFilter().getValue().equals(asList(1,2));
        result = reduce(
                new Filter(asList(1), FilterType.notIn),
                new Filter(asList(1), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);

        //like
        result = reduce(
                new Filter("te", FilterType.like),
                new Filter(Arrays.asList("test"), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.failure);

        //likeStart
        result = reduce(
                new Filter("te", FilterType.likeStart),
                new Filter(Arrays.asList("test"), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.failure);

        //overlap
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterType.overlaps),
                new Filter(Arrays.asList(1, 2), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.failure);

        //contains
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterType.contains),
                new Filter(Arrays.asList(1, 2), FilterType.inOrIsNull));
        assert result.getType().equals(Result.Type.failure);
    }


}
