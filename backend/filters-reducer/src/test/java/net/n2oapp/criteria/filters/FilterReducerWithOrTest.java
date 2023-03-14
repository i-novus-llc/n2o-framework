package net.n2oapp.criteria.filters;

import org.junit.Test;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static net.n2oapp.criteria.filters.FilterReducer.reduce;

/**
 * Тесты для {@link FilterReducer}
 */
public class FilterReducerWithOrTest {


    @Test
    public void testEqOrIsNull() {
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
}
