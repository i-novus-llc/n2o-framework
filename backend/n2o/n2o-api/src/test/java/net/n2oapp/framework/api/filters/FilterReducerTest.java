package net.n2oapp.framework.api.filters;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterReducer;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Result;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static net.n2oapp.framework.api.criteria.filters.FilterReducer.reduce;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Тесты простых случаев в {@link FilterReducer}
 */
class FilterReducerTest {


    @Test
    void testEq() {
        //успех
        Result result = reduce(
                new Filter(1, FilterTypeEnum.EQ),
                new Filter(1, FilterTypeEnum.EQ));
        assertResultEquals(result, FilterTypeEnum.EQ, 1);

        //ошибка
        result = reduce(
                new Filter(1, FilterTypeEnum.EQ),
                new Filter(2, FilterTypeEnum.EQ));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.EQ),
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.IN));
        assertResultEquals(result, FilterTypeEnum.EQ, 1);

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.IN),
                new Filter(1, FilterTypeEnum.EQ));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(1, FilterTypeEnum.EQ),
                new Filter(2, FilterTypeEnum.MORE));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(0, FilterTypeEnum.MORE),
                new Filter(1, FilterTypeEnum.EQ));
        assertResultEquals(result, FilterTypeEnum.EQ, 1);

        //ошибка
        result = reduce(
                new Filter(1, FilterTypeEnum.EQ),
                new Filter(1, FilterTypeEnum.LESS));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(2, FilterTypeEnum.LESS),
                new Filter(1, FilterTypeEnum.EQ));
        assertResultEquals(result, FilterTypeEnum.EQ, 1);

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.IS_NULL),
                new Filter(1, FilterTypeEnum.EQ));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.EQ),
                new Filter(2, FilterTypeEnum.IS_NOT_NULL));
        assertResultEquals(result, FilterTypeEnum.EQ, 1);

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.EQ),
                new Filter(2, FilterTypeEnum.NOT_EQ));
        assertResultEquals(result, FilterTypeEnum.EQ, 1);

        //ошибка
        result = reduce(
                new Filter(1, FilterTypeEnum.NOT_EQ),
                new Filter(1, FilterTypeEnum.EQ));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(2, FilterTypeEnum.EQ),
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.NOT_IN));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.EQ),
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.NOT_IN));
        assertResultEquals(result, FilterTypeEnum.EQ, 1);
    }

    @Test
    void testSimple() {
        //simple не сокращается
        Result result = reduce(
                new Filter(1, FilterTypeEnum.EQ),
                new Filter(1, FilterTypeEnum.SIMPLE));
        assertEquals(Result.TypeEnum.NOT_MERGEABLE, result.getType());
        assertNull(result.getResultFilter());
    }

    @Test
    void testIn() {
        //успех - in to equal
        Result result = reduce(
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.IN),
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.IN));
        assertResultEquals(result, FilterTypeEnum.EQ, 2);

        //успех - in to in
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.IN),
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.IN));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.IN, result.getResultFilter().getType());
        assertEquals(Arrays.asList(2, 3), result.getResultFilter().getValue());

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.IN),
                new Filter(Arrays.asList(9, 10), FilterTypeEnum.IN));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //успех - more, in to equal
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.IN),
                new Filter(3, FilterTypeEnum.MORE));
        assertResultEquals(result, FilterTypeEnum.EQ, 4);


        //ошибка
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.IN),
                new Filter(5, FilterTypeEnum.MORE));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //успех - less, in to in
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.IN),
                new Filter(2, FilterTypeEnum.MORE));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.IN, result.getResultFilter().getType());
        assertEquals(Arrays.asList(3, 4), result.getResultFilter().getValue());

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.IN),
                new Filter(1, FilterTypeEnum.LESS));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.IS_NULL),
                new Filter(1, FilterTypeEnum.IN));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.IS_NOT_NULL),
                new Filter(2, FilterTypeEnum.IN));
        assertResultEquals(result, FilterTypeEnum.IN, 2);

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.NOT_EQ),
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.IN));
        assertResultEquals(result, FilterTypeEnum.EQ, 2);

        //успех
        result = reduce(
                new Filter(3, FilterTypeEnum.NOT_EQ),
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.IN));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.IN, result.getResultFilter().getType());
        assertEquals(Arrays.asList(1, 2), result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(Arrays.asList(1, 2, 8), FilterTypeEnum.NOT_IN),
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.IN));
        assertResultEquals(result, FilterTypeEnum.EQ, 3);

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(1, 2, 8), FilterTypeEnum.NOT_IN),
                new Filter(Arrays.asList(1, 2, 8), FilterTypeEnum.IN));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());
    }

    private static void assertResultEquals(Result result, FilterTypeEnum eq, int expected) {
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(eq, result.getResultFilter().getType());
        assertEquals(expected, result.getResultFilter().getValue());
    }

    @Test
    void testContains() {
        //успех - contains with equal
        Result result = reduce(
                new Filter(Arrays.asList(1, 2, 4), FilterTypeEnum.CONTAINS),
                new Filter(Arrays.asList(1, 3, 2, 4), FilterTypeEnum.EQ));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals(Arrays.asList(1, 3, 2, 4), result.getResultFilter().getValue());

        //ошибка - contains with equal
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.CONTAINS),
                new Filter(2, FilterTypeEnum.EQ));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //успех - contains with contains
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.CONTAINS),
                new Filter(Arrays.asList(1, 2, 4), FilterTypeEnum.CONTAINS));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.CONTAINS, result.getResultFilter().getType());
        assertEquals(Arrays.asList(1, 2, 3, 4), result.getResultFilter().getValue());

        //ошибка - more with contains всегда
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4, 5), FilterTypeEnum.CONTAINS),
                new Filter(1, FilterTypeEnum.MORE));
        assertEquals(Result.TypeEnum.NOT_MERGEABLE, result.getType());
        assertNull(result.getResultFilter());

        //ошибка - less with contains всегда
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.CONTAINS),
                new Filter(5, FilterTypeEnum.LESS));
        assertEquals(Result.TypeEnum.NOT_MERGEABLE, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.IS_NULL),
                new Filter(1, FilterTypeEnum.CONTAINS));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.IS_NOT_NULL),
                new Filter(Arrays.asList(2, 3, 4, 5), FilterTypeEnum.CONTAINS));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.CONTAINS, result.getResultFilter().getType());
        assertEquals(Arrays.asList(2, 3, 4, 5), result.getResultFilter().getValue());

        //ошибка notEq with overlap
        result = reduce(
                new Filter(1, FilterTypeEnum.NOT_EQ),
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.CONTAINS));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());
    }

    @Test
    void testIsNull() {
        //ошибка
        Result result = reduce(
                new Filter(FilterTypeEnum.IS_NULL),
                new Filter(FilterTypeEnum.IS_NOT_NULL));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(FilterTypeEnum.IS_NULL),
                new Filter(1, FilterTypeEnum.NOT_EQ));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.IS_NULL, result.getResultFilter().getType());

        //успех
        result = reduce(
                new Filter(FilterTypeEnum.IS_NULL),
                new Filter(1, FilterTypeEnum.NOT_EQ));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.IS_NULL, result.getResultFilter().getType());
    }

    @Test
    void testNotEq() {
        //успех
        Result result = reduce(
                new Filter(1, FilterTypeEnum.NOT_EQ),
                new Filter(1, FilterTypeEnum.NOT_EQ));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.NOT_EQ, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.NOT_EQ),
                new Filter(2, FilterTypeEnum.NOT_EQ));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.NOT_IN, result.getResultFilter().getType());
        assertEquals(Arrays.asList(1, 2), result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.NOT_IN),
                new Filter(2, FilterTypeEnum.NOT_EQ));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.NOT_IN, result.getResultFilter().getType());
        assertEquals(Arrays.asList(2, 3), result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.NOT_IN),
                new Filter(4, FilterTypeEnum.NOT_EQ));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.NOT_IN, result.getResultFilter().getType());
        assertEquals(Arrays.asList(2, 3, 4), result.getResultFilter().getValue());
    }

    @Test
    void testNotInNotIn() {
        //успех
        Result result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.NOT_IN),
                new Filter(Arrays.asList(2, 3, 4, 5), FilterTypeEnum.NOT_IN));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.NOT_IN, result.getResultFilter().getType());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result.getResultFilter().getValue());
    }

    @Test
    void testLessMore() {
        //успех
        Result result = reduce(
                new Filter(1, FilterTypeEnum.LESS),
                new Filter(2, FilterTypeEnum.LESS));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.LESS, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.MORE),
                new Filter(2, FilterTypeEnum.MORE));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.MORE, result.getResultFilter().getType());
        assertEquals(2, result.getResultFilter().getValue());

        //невозможно сократить выражение
        result = reduce(
                new Filter(1, FilterTypeEnum.MORE),
                new Filter(10, FilterTypeEnum.LESS));
        assertEquals(Result.TypeEnum.NOT_MERGEABLE, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.IS_NULL),
                new Filter(1, FilterTypeEnum.MORE));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.IS_NULL),
                new Filter(1, FilterTypeEnum.LESS));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.IS_NOT_NULL),
                new Filter(2, FilterTypeEnum.MORE));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.MORE, result.getResultFilter().getType());
        assertEquals(2, result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.IS_NOT_NULL),
                new Filter(2, FilterTypeEnum.LESS));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.LESS, result.getResultFilter().getType());
        assertEquals(2, result.getResultFilter().getValue());

    }

    @Test
    void testLike() {
        Result result = reduce(
                new Filter("test", FilterTypeEnum.EQ),
                new Filter("es", FilterTypeEnum.LIKE));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("te", FilterTypeEnum.EQ),
                new Filter("test", FilterTypeEnum.LIKE));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.LIKE),
                new Filter(FilterTypeEnum.IS_NOT_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.LIKE, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("test", FilterTypeEnum.LIKE),
                new Filter(FilterTypeEnum.IS_NULL));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.LIKE),
                new Filter("te", FilterTypeEnum.LIKE));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.LIKE, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("rem", FilterTypeEnum.LIKE),
                new Filter("te", FilterTypeEnum.LIKE));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.LIKE),
                new Filter("te", FilterTypeEnum.LIKE_START));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.LIKE_START, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("te", FilterTypeEnum.LIKE),
                new Filter("test", FilterTypeEnum.LIKE_START));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.LIKE_START, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("rtest", FilterTypeEnum.LIKE),
                new Filter("te", FilterTypeEnum.LIKE_START));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());
    }

    @Test
    void testLikeStart() {
        Result result = reduce(
                new Filter("test", FilterTypeEnum.EQ),
                new Filter("te", FilterTypeEnum.LIKE_START));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.EQ, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("te", FilterTypeEnum.EQ),
                new Filter("test", FilterTypeEnum.LIKE_START));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.EQ),
                new Filter("est", FilterTypeEnum.LIKE_START));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.LIKE_START),
                new Filter(FilterTypeEnum.IS_NOT_NULL));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.LIKE_START, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("test", FilterTypeEnum.LIKE_START),
                new Filter(FilterTypeEnum.IS_NULL));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.LIKE_START),
                new Filter("te", FilterTypeEnum.LIKE_START));
        assertEquals(Result.TypeEnum.SUCCESS, result.getType());
        assertEquals(FilterTypeEnum.LIKE_START, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("rem", FilterTypeEnum.LIKE_START),
                new Filter("te", FilterTypeEnum.LIKE_START));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.LIKE_START),
                new Filter("es", FilterTypeEnum.LIKE_START));
        assertEquals(Result.TypeEnum.CONFLICT, result.getType());
        assertNull(result.getResultFilter());
    }
}
