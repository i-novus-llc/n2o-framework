package net.n2oapp.criteria.filters;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static net.n2oapp.criteria.filters.FilterReducer.reduce;

/**
 * Тесты для {@link FilterReducer}
 */
class FilterReducerWithOrTest {


    @Test
    void testEqOrIsNull() {
        //eq
        Result result = reduce(
                new Filter(1, FilterTypeEnum.eqOrIsNull),
                new Filter(1, FilterTypeEnum.eq));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(1);
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(1, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(1);
        result = reduce(
                new Filter(2, FilterTypeEnum.eq),
                new Filter(1, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.conflict);

        //eqOrIsNull
        result = reduce(
                new Filter(1, FilterTypeEnum.eqOrIsNull),
                new Filter(1, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(1);
        result = reduce(
                new Filter(2, FilterTypeEnum.eqOrIsNull),
                new Filter(1, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.isNull);

        //notEq
        result = reduce(
                new Filter(1, FilterTypeEnum.eqOrIsNull),
                new Filter(1, FilterTypeEnum.notEq));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.isNull);
        result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(1, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.isNull);
        result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(2, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(2);

        //more
        result = reduce(
                new Filter(4, FilterTypeEnum.eqOrIsNull),
                new Filter(1, FilterTypeEnum.more));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(1, FilterTypeEnum.more),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(4, FilterTypeEnum.more),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.conflict);

        //less
        result = reduce(
                new Filter(4, FilterTypeEnum.eqOrIsNull),
                new Filter(7, FilterTypeEnum.less));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(7, FilterTypeEnum.less),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(4, FilterTypeEnum.less),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.conflict);

        //isNull
        result = reduce(
                new Filter(4, FilterTypeEnum.eqOrIsNull),
                new Filter(FilterTypeEnum.isNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.isNull);
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.isNull);

        //isNotNull
        result = reduce(
                new Filter(4, FilterTypeEnum.eqOrIsNull),
                new Filter(FilterTypeEnum.isNotNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(FilterTypeEnum.isNotNull),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(4);

        //in
        result = reduce(
                new Filter(4, FilterTypeEnum.eqOrIsNull),
                new Filter(asList(4, 5, 6), FilterTypeEnum.in));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(asList(4, 5, 6), FilterTypeEnum.in),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(asList(7, 8, 9), FilterTypeEnum.in),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.conflict);

        //notIn
        result = reduce(
                new Filter(4, FilterTypeEnum.eqOrIsNull),
                new Filter(asList(5, 6), FilterTypeEnum.notIn));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(asList(5, 6), FilterTypeEnum.notIn),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eqOrIsNull);
        assert result.getResultFilter().getValue().equals(4);
        result = reduce(
                new Filter(asList(4, 5), FilterTypeEnum.notIn),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.isNull);

        //like
        result = reduce(
                new Filter("es", FilterTypeEnum.like),
                new Filter("test", FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals("test");
        result = reduce(
                new Filter("esy", FilterTypeEnum.like),
                new Filter("test", FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.conflict);

        //likeStart
        result = reduce(
                new Filter("tes", FilterTypeEnum.likeStart),
                new Filter("test", FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals("test");
        result = reduce(
                new Filter("es", FilterTypeEnum.likeStart),
                new Filter("test", FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.conflict);

        //contains
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.contains),
                new Filter(Arrays.asList(1, 2, 4), FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 2, 4));
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.contains),
                new Filter(Arrays.asList(5, 2), FilterTypeEnum.eqOrIsNull));
        assert result.getType().equals(Result.TypeEnum.conflict);
    }
}
