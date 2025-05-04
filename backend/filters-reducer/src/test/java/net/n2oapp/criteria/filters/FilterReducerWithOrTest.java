package net.n2oapp.criteria.filters;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static net.n2oapp.criteria.filters.FilterReducer.reduce;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(1, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());
        result = reduce(
                new Filter(2, FilterTypeEnum.eq),
                new Filter(1, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.conflict, result.getType());

        //eqOrIsNull
        result = reduce(
                new Filter(1, FilterTypeEnum.eqOrIsNull),
                new Filter(1, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eqOrIsNull, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());
        result = reduce(
                new Filter(2, FilterTypeEnum.eqOrIsNull),
                new Filter(1, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.isNull, result.getResultFilter().getType());

        //notEq
        result = reduce(
                new Filter(1, FilterTypeEnum.eqOrIsNull),
                new Filter(1, FilterTypeEnum.notEq));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.isNull, result.getResultFilter().getType());
        result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(1, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.isNull, result.getResultFilter().getType());
        result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(2, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eqOrIsNull, result.getResultFilter().getType());
        assertEquals(2, result.getResultFilter().getValue());

        //more
        result = reduce(
                new Filter(4, FilterTypeEnum.eqOrIsNull),
                new Filter(1, FilterTypeEnum.more));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(1, FilterTypeEnum.more),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(4, FilterTypeEnum.more),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.conflict, result.getType());

        //less
        result = reduce(
                new Filter(4, FilterTypeEnum.eqOrIsNull),
                new Filter(7, FilterTypeEnum.less));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(7, FilterTypeEnum.less),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(4, FilterTypeEnum.less),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.conflict, result.getType());

        //isNull
        result = reduce(
                new Filter(4, FilterTypeEnum.eqOrIsNull),
                new Filter(FilterTypeEnum.isNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.isNull, result.getResultFilter().getType());
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.isNull, result.getResultFilter().getType());

        //isNotNull
        result = reduce(
                new Filter(4, FilterTypeEnum.eqOrIsNull),
                new Filter(FilterTypeEnum.isNotNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(FilterTypeEnum.isNotNull),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());

        //in
        result = reduce(
                new Filter(4, FilterTypeEnum.eqOrIsNull),
                new Filter(asList(4, 5, 6), FilterTypeEnum.in));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(asList(4, 5, 6), FilterTypeEnum.in),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(asList(7, 8, 9), FilterTypeEnum.in),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.conflict, result.getType());

        //notIn
        result = reduce(
                new Filter(4, FilterTypeEnum.eqOrIsNull),
                new Filter(asList(5, 6), FilterTypeEnum.notIn));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eqOrIsNull, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(asList(5, 6), FilterTypeEnum.notIn),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eqOrIsNull, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(asList(4, 5), FilterTypeEnum.notIn),
                new Filter(4, FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.isNull, result.getResultFilter().getType());

        //like
        result = reduce(
                new Filter("es", FilterTypeEnum.like),
                new Filter("test", FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());
        result = reduce(
                new Filter("esy", FilterTypeEnum.like),
                new Filter("test", FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.conflict, result.getType());

        //likeStart
        result = reduce(
                new Filter("tes", FilterTypeEnum.likeStart),
                new Filter("test", FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());
        result = reduce(
                new Filter("es", FilterTypeEnum.likeStart),
                new Filter("test", FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.conflict, result.getType());

        //contains
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.contains),
                new Filter(Arrays.asList(1, 2, 4), FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(Arrays.asList(1, 2, 4), result.getResultFilter().getValue());
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.contains),
                new Filter(Arrays.asList(5, 2), FilterTypeEnum.eqOrIsNull));
        assertEquals(Result.TypeEnum.conflict, result.getType());
    }
}
