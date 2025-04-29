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
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(1);

        //ошибка
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(2, FilterTypeEnum.eq));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.in));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(1);

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.in),
                new Filter(1, FilterTypeEnum.eq));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(2, FilterTypeEnum.more));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(0, FilterTypeEnum.more),
                new Filter(1, FilterTypeEnum.eq));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(1);

        //ошибка
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(1, FilterTypeEnum.less));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(2, FilterTypeEnum.less),
                new Filter(1, FilterTypeEnum.eq));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(1);

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.eq));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(2, FilterTypeEnum.isNotNull));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(1);


        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(2, FilterTypeEnum.notEq));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(1);

        //ошибка
        result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(1, FilterTypeEnum.eq));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(2, FilterTypeEnum.eq),
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.notIn));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.notIn));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(1);
    }

    @Test
    void testSimple() {
        //simple не сокращается
        Result result = reduce(
                new Filter(1, FilterTypeEnum.eq),
                new Filter(1, FilterTypeEnum.simple));
        assert result.getType().equals(Result.TypeEnum.notMergeable);
        assert result.getResultFilter() == null;
    }

    @Test
    void testIn() {

        //успех - in to equal
        Result result = reduce(
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.in),
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.in));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(2);

        //успех - in to in
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.in),
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.in));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.in);
        assert result.getResultFilter().getValue().equals(Arrays.asList(2, 3));

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.in),
                new Filter(Arrays.asList(9, 10), FilterTypeEnum.in));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;


        //успех - more, in to equal
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.in),
                new Filter(3, FilterTypeEnum.more));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(4);


        //ошибка
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.in),
                new Filter(5, FilterTypeEnum.more));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;


        //успех - less, in to in
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.in),
                new Filter(2, FilterTypeEnum.more));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.in);
        assert result.getResultFilter().getValue().equals(Arrays.asList(3, 4));


        //ошибка
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.in),
                new Filter(1, FilterTypeEnum.less));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.in));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.isNotNull),
                new Filter(2, FilterTypeEnum.in));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.in);
        assert result.getResultFilter().getValue().equals(2);

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.in));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(2);

        //успех
        result = reduce(
                new Filter(3, FilterTypeEnum.notEq),
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.in));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.in);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 2));

        //успех
        result = reduce(
                new Filter(Arrays.asList(1, 2, 8), FilterTypeEnum.notIn),
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.in));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(3);

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(1, 2, 8), FilterTypeEnum.notIn),
                new Filter(Arrays.asList(1, 2, 8), FilterTypeEnum.in));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;
    }

    @Test
    void testContains() {
        //успех - contains with equal
        Result result = reduce(
                new Filter(Arrays.asList(1, 2, 4), FilterTypeEnum.contains),
                new Filter(Arrays.asList(1, 3, 2, 4), FilterTypeEnum.eq));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.eq);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 3, 2, 4));

        //ошибка - contains with equal
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.contains),
                new Filter(2, FilterTypeEnum.eq));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //успех - contains with contains
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.contains),
                new Filter(Arrays.asList(1, 2, 4), FilterTypeEnum.contains));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.contains);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 2, 3, 4));

        //ошибка - more with contains всегда
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4, 5), FilterTypeEnum.contains),
                new Filter(1, FilterTypeEnum.more));
        assert result.getType().equals(Result.TypeEnum.notMergeable);
        assert result.getResultFilter() == null;

        //ошибка - less with contains всегда
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterTypeEnum.contains),
                new Filter(5, FilterTypeEnum.less));
        assert result.getType().equals(Result.TypeEnum.notMergeable);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.contains));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.isNotNull),
                new Filter(Arrays.asList(2, 3, 4, 5), FilterTypeEnum.contains));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.contains);
        assert result.getResultFilter().getValue().equals(Arrays.asList(2, 3, 4, 5));

        //ошибка notEq with overlap
        result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(Arrays.asList(1, 2), FilterTypeEnum.contains));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;
    }


    @Test
    void testIsNull() {
        //ошибка
        Result result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(FilterTypeEnum.isNotNull));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.notEq));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.isNull);

        //успех
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.notEq));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.isNull);
    }


    @Test
    void testNotEq() {
        //успех
        Result result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(1, FilterTypeEnum.notEq));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.notEq);
        assert result.getResultFilter().getValue().equals(1);

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.notEq),
                new Filter(2, FilterTypeEnum.notEq));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.notIn);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 2));

        //успех
        result = reduce(
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.notIn),
                new Filter(2, FilterTypeEnum.notEq));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.notIn);
        assert result.getResultFilter().getValue().equals(Arrays.asList(2, 3));

        //успех
        result = reduce(
                new Filter(Arrays.asList(2, 3), FilterTypeEnum.notIn),
                new Filter(4, FilterTypeEnum.notEq));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.notIn);
        assert result.getResultFilter().getValue().equals(Arrays.asList(2, 3, 4));

    }

    @Test
    void testNotInNotIn() {
        //успех
        Result result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterTypeEnum.notIn),
                new Filter(Arrays.asList(2, 3, 4, 5), FilterTypeEnum.notIn));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.notIn);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 2, 3, 4, 5));


    }


    @Test
    void testLessMore() {

        //успех
        Result result = reduce(
                new Filter(1, FilterTypeEnum.less),
                new Filter(2, FilterTypeEnum.less));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.less);
        assert result.getResultFilter().getValue().equals(1);

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.more),
                new Filter(2, FilterTypeEnum.more));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.more);
        assert result.getResultFilter().getValue().equals(2);

        //невозможно сократить выражение
        result = reduce(
                new Filter(1, FilterTypeEnum.more),
                new Filter(10, FilterTypeEnum.less));
        assert result.getType().equals(Result.TypeEnum.notMergeable);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.more));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(FilterTypeEnum.isNull),
                new Filter(1, FilterTypeEnum.less));
        assert result.getType().equals(Result.TypeEnum.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.isNotNull),
                new Filter(2, FilterTypeEnum.more));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.more);
        assert result.getResultFilter().getValue().equals(2);

        //успех
        result = reduce(
                new Filter(1, FilterTypeEnum.isNotNull),
                new Filter(2, FilterTypeEnum.less));
        assert result.getType().equals(Result.TypeEnum.success);
        assert result.getResultFilter().getType().equals(FilterTypeEnum.less);
        assert result.getResultFilter().getValue().equals(2);

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
