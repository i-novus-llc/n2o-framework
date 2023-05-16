package net.n2oapp.criteria.filters;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static net.n2oapp.criteria.filters.FilterReducer.reduce;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Тесты простых случаев в {@link FilterReducer}
 */
public class FilterReducerTest {


    @Test
    void testEq() {

        //успех
        Result result = reduce(
                new Filter(1, FilterType.eq),
                new Filter(1, FilterType.eq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);

        //ошибка
        result = reduce(
                new Filter(1, FilterType.eq),
                new Filter(2, FilterType.eq));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(1, FilterType.eq),
                new Filter(Arrays.asList(1, 2), FilterType.in));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(2, 3), FilterType.in),
                new Filter(1, FilterType.eq));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(1, FilterType.eq),
                new Filter(2, FilterType.more));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(0, FilterType.more),
                new Filter(1, FilterType.eq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);

        //ошибка
        result = reduce(
                new Filter(1, FilterType.eq),
                new Filter(1, FilterType.less));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(2, FilterType.less),
                new Filter(1, FilterType.eq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);

        //ошибка
        result = reduce(
                new Filter(FilterType.isNull),
                new Filter(1, FilterType.eq));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(1, FilterType.eq),
                new Filter(2, FilterType.isNotNull));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);


        //успех
        result = reduce(
                new Filter(1, FilterType.eq),
                new Filter(2, FilterType.notEq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);

        //ошибка
        result = reduce(
                new Filter(1, FilterType.notEq),
                new Filter(1, FilterType.eq));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(2, FilterType.eq),
                new Filter(Arrays.asList(2, 3), FilterType.notIn));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(1, FilterType.eq),
                new Filter(Arrays.asList(2, 3), FilterType.notIn));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(1);
    }

    @Test
    void testSimple() {
        //simple не сокращается
        Result result = reduce(
                new Filter(1, FilterType.eq),
                new Filter(1, FilterType.simple));
        assert result.getType().equals(Result.Type.notMergeable);
        assert result.getResultFilter() == null;
    }

    @Test
    void testIn() {

        //успех - in to equal
        Result result = reduce(
                new Filter(Arrays.asList(1, 2), FilterType.in),
                new Filter(Arrays.asList(2, 3), FilterType.in));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(2);

        //успех - in to in
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterType.in),
                new Filter(Arrays.asList(2, 3, 4), FilterType.in));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.in);
        assert result.getResultFilter().getValue().equals(Arrays.asList(2, 3));

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterType.in),
                new Filter(Arrays.asList(9, 10), FilterType.in));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;


        //успех - more, in to equal
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterType.in),
                new Filter(3, FilterType.more));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(4);


        //ошибка
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterType.in),
                new Filter(5, FilterType.more));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;


        //успех - less, in to in
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterType.in),
                new Filter(2, FilterType.more));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.in);
        assert result.getResultFilter().getValue().equals(Arrays.asList(3, 4));


        //ошибка
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterType.in),
                new Filter(1, FilterType.less));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(FilterType.isNull),
                new Filter(1, FilterType.in));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(1, FilterType.isNotNull),
                new Filter(2, FilterType.in));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.in);
        assert result.getResultFilter().getValue().equals(2);

        //успех
        result = reduce(
                new Filter(1, FilterType.notEq),
                new Filter(Arrays.asList(1, 2), FilterType.in));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(2);

        //успех
        result = reduce(
                new Filter(3, FilterType.notEq),
                new Filter(Arrays.asList(1, 2), FilterType.in));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.in);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 2));

        //успех
        result = reduce(
                new Filter(Arrays.asList(1, 2, 8), FilterType.notIn),
                new Filter(Arrays.asList(1, 2, 3), FilterType.in));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(3);

        //ошибка
        result = reduce(
                new Filter(Arrays.asList(1, 2, 8), FilterType.notIn),
                new Filter(Arrays.asList(1, 2, 8), FilterType.in));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;
    }

    @Test
    void testContains() {
        //успех - contains with equal
        Result result = reduce(
                new Filter(Arrays.asList(1, 2, 4), FilterType.contains),
                new Filter(Arrays.asList(1, 3, 2, 4), FilterType.eq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.eq);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 3, 2, 4));

        //ошибка - contains with equal
        result = reduce(
                new Filter(Arrays.asList(1, 2), FilterType.contains),
                new Filter(2, FilterType.eq));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //успех - contains with contains
        result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterType.contains),
                new Filter(Arrays.asList(1, 2, 4), FilterType.contains));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.contains);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 2, 3, 4));

        //ошибка - more with contains всегда
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4, 5), FilterType.contains),
                new Filter(1, FilterType.more));
        assert result.getType().equals(Result.Type.notMergeable);
        assert result.getResultFilter() == null;

        //ошибка - less with contains всегда
        result = reduce(
                new Filter(Arrays.asList(2, 3, 4), FilterType.contains),
                new Filter(5, FilterType.less));
        assert result.getType().equals(Result.Type.notMergeable);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(FilterType.isNull),
                new Filter(1, FilterType.contains));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(1, FilterType.isNotNull),
                new Filter(Arrays.asList(2, 3, 4, 5), FilterType.contains));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.contains);
        assert result.getResultFilter().getValue().equals(Arrays.asList(2, 3, 4, 5));

        //ошибка notEq with overlap
        result = reduce(
                new Filter(1, FilterType.notEq),
                new Filter(Arrays.asList(1, 2), FilterType.contains));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;
    }


    @Test
    void testIsNull() {
        //ошибка
        Result result = reduce(
                new Filter(FilterType.isNull),
                new Filter(FilterType.isNotNull));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(FilterType.isNull),
                new Filter(1, FilterType.notEq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);

        //успех
        result = reduce(
                new Filter(FilterType.isNull),
                new Filter(1, FilterType.notEq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.isNull);
    }


    @Test
    void testNotEq() {
        //успех
        Result result = reduce(
                new Filter(1, FilterType.notEq),
                new Filter(1, FilterType.notEq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.notEq);
        assert result.getResultFilter().getValue().equals(1);

        //успех
        result = reduce(
                new Filter(1, FilterType.notEq),
                new Filter(2, FilterType.notEq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.notIn);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 2));

        //успех
        result = reduce(
                new Filter(Arrays.asList(2, 3), FilterType.notIn),
                new Filter(2, FilterType.notEq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.notIn);
        assert result.getResultFilter().getValue().equals(Arrays.asList(2, 3));

        //успех
        result = reduce(
                new Filter(Arrays.asList(2, 3), FilterType.notIn),
                new Filter(4, FilterType.notEq));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.notIn);
        assert result.getResultFilter().getValue().equals(Arrays.asList(2, 3, 4));

    }

    @Test
    void testNotInNotIn() {
        //успех
        Result result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterType.notIn),
                new Filter(Arrays.asList(2, 3, 4, 5), FilterType.notIn));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.notIn);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 2, 3, 4, 5));


    }


    @Test
    void testLessMore() {

        //успех
        Result result = reduce(
                new Filter(1, FilterType.less),
                new Filter(2, FilterType.less));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.less);
        assert result.getResultFilter().getValue().equals(1);

        //успех
        result = reduce(
                new Filter(1, FilterType.more),
                new Filter(2, FilterType.more));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.more);
        assert result.getResultFilter().getValue().equals(2);

        //невозможно сократить выражение
        result = reduce(
                new Filter(1, FilterType.more),
                new Filter(10, FilterType.less));
        assert result.getType().equals(Result.Type.notMergeable);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(FilterType.isNull),
                new Filter(1, FilterType.more));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //ошибка
        result = reduce(
                new Filter(FilterType.isNull),
                new Filter(1, FilterType.less));
        assert result.getType().equals(Result.Type.conflict);
        assert result.getResultFilter() == null;

        //успех
        result = reduce(
                new Filter(1, FilterType.isNotNull),
                new Filter(2, FilterType.more));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.more);
        assert result.getResultFilter().getValue().equals(2);

        //успех
        result = reduce(
                new Filter(1, FilterType.isNotNull),
                new Filter(2, FilterType.less));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.less);
        assert result.getResultFilter().getValue().equals(2);

    }

    @Test
    void testLike() {
        Result result = reduce(
                new Filter("test", FilterType.eq),
                new Filter("es", FilterType.like));
        assertEquals(result.getType(), Result.Type.success);
        assertEquals(result.getResultFilter().getType(), FilterType.eq);
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("te", FilterType.eq),
                new Filter("test", FilterType.like));
        assertEquals(result.getType(), Result.Type.conflict);
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterType.like),
                new Filter(FilterType.isNotNull));
        assertEquals(result.getType(), Result.Type.success);
        assertEquals(result.getResultFilter().getType(), FilterType.like);
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("test", FilterType.like),
                new Filter(FilterType.isNull));
        assertEquals(result.getType(), Result.Type.conflict);
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterType.like),
                new Filter("te", FilterType.like));
        assertEquals(result.getType(), Result.Type.success);
        assertEquals(result.getResultFilter().getType(), FilterType.like);
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("rem", FilterType.like),
                new Filter("te", FilterType.like));
        assertEquals(result.getType(), Result.Type.conflict);
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterType.like),
                new Filter("te", FilterType.likeStart));
        assertEquals(result.getType(), Result.Type.success);
        assertEquals(result.getResultFilter().getType(), FilterType.likeStart);
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("te", FilterType.like),
                new Filter("test", FilterType.likeStart));
        assertEquals(result.getType(), Result.Type.success);
        assertEquals(result.getResultFilter().getType(), FilterType.likeStart);
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("rtest", FilterType.like),
                new Filter("te", FilterType.likeStart));
        assertEquals(result.getType(), Result.Type.conflict);
        assertNull(result.getResultFilter());
    }

    @Test
    void testLikeStart() {
        Result result = reduce(
                new Filter("test", FilterType.eq),
                new Filter("te", FilterType.likeStart));
        assertEquals(result.getType(), Result.Type.success);
        assertEquals(result.getResultFilter().getType(), FilterType.eq);
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("te", FilterType.eq),
                new Filter("test", FilterType.likeStart));
        assertEquals(result.getType(), Result.Type.conflict);
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterType.eq),
                new Filter("est", FilterType.likeStart));
        assertEquals(result.getType(), Result.Type.conflict);
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterType.likeStart),
                new Filter(FilterType.isNotNull));
        assertEquals(result.getType(), Result.Type.success);
        assertEquals(result.getResultFilter().getType(), FilterType.likeStart);
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("test", FilterType.likeStart),
                new Filter(FilterType.isNull));
        assertEquals(result.getType(), Result.Type.conflict);
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterType.likeStart),
                new Filter("te", FilterType.likeStart));
        assertEquals(result.getType(), Result.Type.success);
        assertEquals(result.getResultFilter().getType(), FilterType.likeStart);
        assertEquals("test", result.getResultFilter().getValue());

        result = reduce(
                new Filter("rem", FilterType.likeStart),
                new Filter("te", FilterType.likeStart));
        assertEquals(result.getType(), Result.Type.conflict);
        assertNull(result.getResultFilter());

        result = reduce(
                new Filter("test", FilterType.likeStart),
                new Filter("es", FilterType.likeStart));
        assertEquals(result.getType(), Result.Type.conflict);
        assertNull(result.getResultFilter());
    }
}
