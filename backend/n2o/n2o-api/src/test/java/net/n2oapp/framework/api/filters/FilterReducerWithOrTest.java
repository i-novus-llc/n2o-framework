package net.n2oapp.framework.api.filters;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterReducer;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Result;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static net.n2oapp.framework.api.criteria.filters.FilterReducer.reduce;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для {@link FilterReducer}
 */
class FilterReducerWithOrTest {

    @Test
    void testEqOrIsNull() {
        //eq
        Result result = reduce(
                new Filter(1, FilterTypeEnum.EQ_OR_IS_NULL),
                new Filter(1, FilterTypeEnum.EQ));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());
        result = reduce(
                new Filter(1, FilterTypeEnum.EQ),
                new Filter(1, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());
        result = reduce(
                new Filter(2, FilterTypeEnum.EQ),
                new Filter(1, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());

        //eqOrIsNull
        result = reduce(
                new Filter(1, FilterTypeEnum.EQ_OR_IS_NULL),
                new Filter(1, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ_OR_IS_NULL, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());
        result = reduce(
                new Filter(2, FilterTypeEnum.EQ_OR_IS_NULL),
                new Filter(1, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.IS_NULL, result.getResultFilter().getType());

        //notEq
        result = reduce(
                new Filter(1, FilterTypeEnum.EQ_OR_IS_NULL),
                new Filter(1, FilterTypeEnum.NOT_EQ));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.IS_NULL, result.getResultFilter().getType());
        result = reduce(
                new Filter(1, FilterTypeEnum.NOT_EQ),
                new Filter(1, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.IS_NULL, result.getResultFilter().getType());
        result = reduce(
                new Filter(1, FilterTypeEnum.NOT_EQ),
                new Filter(2, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ_OR_IS_NULL, result.getResultFilter().getType());
        assertEquals(2, result.getResultFilter().getValue());
    }

    @Test
    void testMoreLessNull() {
        //more
        Result result = reduce(
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL),
                new Filter(1, FilterTypeEnum.MORE));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(1, FilterTypeEnum.MORE),
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(4, FilterTypeEnum.MORE),
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());

        //less
        result = reduce(
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL),
                new Filter(7, FilterTypeEnum.LESS));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(7, FilterTypeEnum.LESS),
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(4, FilterTypeEnum.LESS),
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());

        //isNull
        result = reduce(
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL),
                new Filter(FilterTypeEnum.IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.IS_NULL, result.getResultFilter().getType());
        result = reduce(
                new Filter(FilterTypeEnum.IS_NULL),
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.IS_NULL, result.getResultFilter().getType());

        //isNotNull
        result = reduce(
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL),
                new Filter(FilterTypeEnum.IS_NOT_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(FilterTypeEnum.IS_NOT_NULL),
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
    }

    @Test
    void testIn() {
        //in
        Result result = reduce(
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL),
                new Filter(asList(4, 5, 6), FilterTypeEnum.IN));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(asList(4, 5, 6), FilterTypeEnum.IN),
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(asList(7, 8, 9), FilterTypeEnum.IN),
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());

        //notIn
        result = reduce(
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL),
                new Filter(asList(5, 6), FilterTypeEnum.NOT_IN));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ_OR_IS_NULL, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(asList(5, 6), FilterTypeEnum.NOT_IN),
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ_OR_IS_NULL, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());
        result = reduce(
                new Filter(asList(4, 5), FilterTypeEnum.NOT_IN),
                new Filter(4, FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.IS_NULL, result.getResultFilter().getType());
    }

    @Test
    void testLikeContains() {
        //like
        Result result = reduce(
                new Filter("es", FilterTypeEnum.LIKE),
                new Filter("test", FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());
        result = reduce(
                new Filter("esy", FilterTypeEnum.LIKE),
                new Filter("test", FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());

        //likeStart
        result = reduce(
                new Filter("tes", FilterTypeEnum.LIKE_START),
                new Filter("test", FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());
        result = reduce(
                new Filter("es", FilterTypeEnum.LIKE_START),
                new Filter("test", FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());

        //contains
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.CONTAINS),
                new Filter(Arrays.asList(1, 2, 4), FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals(Arrays.asList(1, 2, 4), result.getResultFilter().getValue());
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.CONTAINS),
                new Filter(Arrays.asList(5, 2), FilterTypeEnum.EQ_OR_IS_NULL));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
    }
}
