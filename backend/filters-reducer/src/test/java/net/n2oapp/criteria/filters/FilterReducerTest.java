package net.n2oapp.criteria.filters;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static net.n2oapp.criteria.filters.FilterReducer.reduce;
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
                new Filter(1, FilterTypeEnum.eq),
                new Filter(1, FilterTypeEnum.eq));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());

        //ошибка
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(2, FilterTypeEnum.eq));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.in));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.in),
                new Filter(1, FilterTypeEnum.eq));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(2, FilterTypeEnum.more));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(0, FilterTypeEnum.more),
                new Filter(1, FilterTypeEnum.eq));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());

        //ошибка
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(1, FilterTypeEnum.less));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(2, FilterTypeEnum.less),
                new Filter(1, FilterTypeEnum.eq));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.eq));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(2, FilterTypeEnum.isNotNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(2, FilterTypeEnum.notEq));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());

        //ошибка
        result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(1, FilterTypeEnum.eq));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(2, FilterTypeEnum.eq),
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.notIn));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.notIn));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());
    }

    @Test
    void testSimple() {
        //simple не сокращается
        Result result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(1, FilterTypeEnum.simple));
        assertEquals(Result.TypeEnum.notMergeable, result.getType());
        assertNull(result.getResultFilter());
    }

    @Test
    void testIn() {
        //успех - in to equal
        Result result = reduce(
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.in),
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.in));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(2, result.getResultFilter().getValue());

        //успех - in to in
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.in),
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.in));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.in, result.getResultFilter().getType());
        assertEquals(Arrays.asList(2, 3), result.getResultFilter().getValue());

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.in),
                new Filter(Arrays.asList(9, 10), FilterTypeEnum.in));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //успех - more, in to equal
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.in),
                new Filter(3, FilterTypeEnum.more));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(4, result.getResultFilter().getValue());


        //ошибка
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.in),
                new Filter(5, FilterTypeEnum.more));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //успех - less, in to in
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.in),
                new Filter(2, FilterTypeEnum.more));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.in, result.getResultFilter().getType());
        assertEquals(Arrays.asList(3, 4), result.getResultFilter().getValue());

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.in),
                new Filter(1, FilterTypeEnum.less));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.in));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.isNotNull),
                new Filter(2, FilterTypeEnum.in));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.in, result.getResultFilter().getType());
        assertEquals(2, result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.in));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(2, result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(3, FilterTypeEnum.notEq),
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.in));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.in, result.getResultFilter().getType());
        assertEquals(Arrays.asList(1, 2), result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(Arrays.asList(1, 2, 8), FilterTypeEnum.notIn),
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.in));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(3, result.getResultFilter().getValue());

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(1, 2, 8), FilterTypeEnum.notIn),
                new Filter(Arrays.asList(1, 2, 8), FilterTypeEnum.in));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());
    }

    @Test
    void testContains() {
        //успех - contains with equal
        Result result = reduce(
                new Filter(Arrays.asList(1, 2, 4), FilterTypeEnum.contains),
                new Filter(Arrays.asList(1, 3, 2, 4), FilterTypeEnum.eq));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals(Arrays.asList(1, 3, 2, 4), result.getResultFilter().getValue());

        //ошибка - contains with equal
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.contains),
                new Filter(2, FilterTypeEnum.eq));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //успех - contains with contains
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.contains),
                new Filter(Arrays.asList(1, 2, 4), FilterTypeEnum.contains));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.contains, result.getResultFilter().getType());
        assertEquals(Arrays.asList(1, 2, 3, 4), result.getResultFilter().getValue());

        //ошибка - more with contains всегда
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4, 5), FilterTypeEnum.contains),
                new Filter(1, FilterTypeEnum.more));
        assertEquals(Result.TypeEnum.notMergeable, result.getType());
        assertNull(result.getResultFilter());

        //ошибка - less with contains всегда
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.contains),
                new Filter(5, FilterTypeEnum.less));
        assertEquals(Result.TypeEnum.notMergeable, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.contains));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.isNotNull),
                new Filter(Arrays.asList(2, 3, 4, 5), FilterTypeEnum.contains));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.contains, result.getResultFilter().getType());
        assertEquals(Arrays.asList(2, 3, 4, 5), result.getResultFilter().getValue());

        //ошибка notEq with overlap
        result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.contains));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());
    }

    @Test
    void testIsNull() {
        //ошибка
        Result result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(FilterTypeEnum.isNotNull));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.notEq));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.isNull, result.getResultFilter().getType());

        //успех
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.notEq));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.isNull, result.getResultFilter().getType());
    }

    @Test
    void testNotEq() {
        //успех
        Result result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(1, FilterTypeEnum.notEq));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.notEq, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(2, FilterTypeEnum.notEq));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.notIn, result.getResultFilter().getType());
        assertEquals(Arrays.asList(1, 2), result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.notIn),
                new Filter(2, FilterTypeEnum.notEq));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.notIn, result.getResultFilter().getType());
        assertEquals(Arrays.asList(2, 3), result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.notIn),
                new Filter(4, FilterTypeEnum.notEq));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.notIn, result.getResultFilter().getType());
        assertEquals(Arrays.asList(2, 3, 4), result.getResultFilter().getValue());
    }

    @Test
    void testNotInNotIn() {
        //успех
        Result result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.notIn),
                new Filter(Arrays.asList(2, 3, 4, 5), FilterTypeEnum.notIn));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.notIn, result.getResultFilter().getType());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result.getResultFilter().getValue());
    }

    @Test
    void testLessMore() {
        //успех
        Result result = reduce(
                new Filter(1, FilterTypeEnum.less),
                new Filter(2, FilterTypeEnum.less));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.less, result.getResultFilter().getType());
        assertEquals(1, result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.more),
                new Filter(2, FilterTypeEnum.more));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.more, result.getResultFilter().getType());
        assertEquals(2, result.getResultFilter().getValue());

        //невозможно сократить выражение
        result = reduce(
                new Filter(1, FilterTypeEnum.more),
                new Filter(10, FilterTypeEnum.less));
        assertEquals(Result.TypeEnum.notMergeable, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.more));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.less));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.isNotNull),
                new Filter(2, FilterTypeEnum.more));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.more, result.getResultFilter().getType());
        assertEquals(2, result.getResultFilter().getValue());

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.isNotNull),
                new Filter(2, FilterTypeEnum.less));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.less, result.getResultFilter().getType());
        assertEquals(2, result.getResultFilter().getValue());

    }

    @Test
    void testLike() {
        Result result = reduce(
                new Filter("test", FilterTypeEnum.eq),
                new Filter("es", FilterTypeEnum.like));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("te", FilterTypeEnum.eq),
                new Filter("test", FilterTypeEnum.like));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.like),
                new Filter(FilterTypeEnum.isNotNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.like, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("test", FilterTypeEnum.like),
                new Filter(FilterTypeEnum.isNull));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.like),
                new Filter("te", FilterTypeEnum.like));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.like, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("rem", FilterTypeEnum.like),
                new Filter("te", FilterTypeEnum.like));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.like),
                new Filter("te", FilterTypeEnum.likeStart));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.likeStart, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("te", FilterTypeEnum.like),
                new Filter("test", FilterTypeEnum.likeStart));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.likeStart, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("rtest", FilterTypeEnum.like),
                new Filter("te", FilterTypeEnum.likeStart));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());
    }

    @Test
    void testLikeStart() {
        Result result = reduce(
                new Filter("test", FilterTypeEnum.eq),
                new Filter("te", FilterTypeEnum.likeStart));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.eq, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("te", FilterTypeEnum.eq),
                new Filter("test", FilterTypeEnum.likeStart));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.eq),
                new Filter("est", FilterTypeEnum.likeStart));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.likeStart),
                new Filter(FilterTypeEnum.isNotNull));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.likeStart, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("test", FilterTypeEnum.likeStart),
                new Filter(FilterTypeEnum.isNull));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.likeStart),
                new Filter("te", FilterTypeEnum.likeStart));
        assertEquals(Result.TypeEnum.success, result.getType());
        assertEquals(FilterTypeEnum.likeStart, result.getResultFilter().getType());
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("rem", FilterTypeEnum.likeStart),
                new Filter("te", FilterTypeEnum.likeStart));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterTypeEnum.likeStart),
                new Filter("es", FilterTypeEnum.likeStart));
        assertEquals(Result.TypeEnum.conflict, result.getType());
        assertNull(result.getResultFilter());
    }
}
