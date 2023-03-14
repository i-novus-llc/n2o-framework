package net.n2oapp.criteria.filters;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static net.n2oapp.criteria.filters.FilterReducer.reduce;

/**
 * Тесты простых случаев в {@link FilterReducer}
 */
public class FilterReducerTest {


    @Test
    public void testEq() throws Exception {

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
    public void testSimple() throws Exception {
        //simple не сокращается
        Result result = reduce(
                new Filter(1, FilterType.eq),
                new Filter(1, FilterType.simple));
        assert result.getType().equals(Result.Type.notMergeable);
        assert result.getResultFilter() == null;
    }

    @Test
    public void testIn() throws Exception {

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
    public void testContains() throws Exception {
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
    public void testIsNull() throws Exception {
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
    public void testNotEq() throws Exception {
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
    public void testNotInNotIn() throws Exception {
        //успех
        Result result = reduce(
                new Filter(Arrays.asList(1, 2, 3), FilterType.notIn),
                new Filter(Arrays.asList(2, 3, 4, 5), FilterType.notIn));
        assert result.getType().equals(Result.Type.success);
        assert result.getResultFilter().getType().equals(FilterType.notIn);
        assert result.getResultFilter().getValue().equals(Arrays.asList(1, 2, 3, 4, 5));


    }


    @Test
    public void testLessMore() throws Exception {

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
    public void testLike() throws Exception {
        Result result = reduce(
                new Filter("test", FilterType.eq),
                new Filter("es", FilterType.like));
        Assert.assertTrue(result.getType().equals(Result.Type.success));
        Assert.assertTrue(result.getResultFilter().getType().equals(FilterType.eq));
        Assert.assertTrue(result.getResultFilter().getValue().equals("test"));

        result = reduce(
                new Filter("te", FilterType.eq),
                new Filter("test", FilterType.like));
        Assert.assertTrue(result.getType().equals(Result.Type.conflict));
        Assert.assertTrue(result.getResultFilter() == null);

        result = reduce(
                new Filter("test", FilterType.like),
                new Filter(FilterType.isNotNull));
        Assert.assertTrue(result.getType().equals(Result.Type.success));
        Assert.assertTrue(result.getResultFilter().getType().equals(FilterType.like));
        Assert.assertTrue(result.getResultFilter().getValue().equals("test"));

        result = reduce(
                new Filter("test", FilterType.like),
                new Filter(FilterType.isNull));
        Assert.assertTrue(result.getType().equals(Result.Type.conflict));
        Assert.assertTrue(result.getResultFilter() == null);

        result = reduce(
                new Filter("test", FilterType.like),
                new Filter("te", FilterType.like));
        Assert.assertTrue(result.getType().equals(Result.Type.success));
        Assert.assertTrue(result.getResultFilter().getType().equals(FilterType.like));
        Assert.assertTrue(result.getResultFilter().getValue().equals("test"));

        result = reduce(
                new Filter("rem", FilterType.like),
                new Filter("te", FilterType.like));
        Assert.assertTrue(result.getType().equals(Result.Type.conflict));
        Assert.assertTrue(result.getResultFilter() == null);

        result = reduce(
                new Filter("test", FilterType.like),
                new Filter("te", FilterType.likeStart));
        Assert.assertTrue(result.getType().equals(Result.Type.success));
        Assert.assertTrue(result.getResultFilter().getType().equals(FilterType.likeStart));
        Assert.assertTrue(result.getResultFilter().getValue().equals("test"));

        result = reduce(
                new Filter("te", FilterType.like),
                new Filter("test", FilterType.likeStart));
        Assert.assertTrue(result.getType().equals(Result.Type.success));
        Assert.assertTrue(result.getResultFilter().getType().equals(FilterType.likeStart));
        Assert.assertTrue(result.getResultFilter().getValue().equals("test"));

        result = reduce(
                new Filter("rtest", FilterType.like),
                new Filter("te", FilterType.likeStart));
        Assert.assertTrue(result.getType().equals(Result.Type.conflict));
        Assert.assertTrue(result.getResultFilter() == null);
    }

    @Test
    public void testLikeStart() throws Exception {
        Result result = reduce(
                new Filter("test", FilterType.eq),
                new Filter("te", FilterType.likeStart));
        Assert.assertTrue(result.getType().equals(Result.Type.success));
        Assert.assertTrue(result.getResultFilter().getType().equals(FilterType.eq));
        Assert.assertTrue(result.getResultFilter().getValue().equals("test"));

        result = reduce(
                new Filter("te", FilterType.eq),
                new Filter("test", FilterType.likeStart));
        Assert.assertTrue(result.getType().equals(Result.Type.conflict));
        Assert.assertTrue(result.getResultFilter() == null);

        result = reduce(
                new Filter("test", FilterType.eq),
                new Filter("est", FilterType.likeStart));
        Assert.assertTrue(result.getType().equals(Result.Type.conflict));
        Assert.assertTrue(result.getResultFilter() == null);

        result = reduce(
                new Filter("test", FilterType.likeStart),
                new Filter(FilterType.isNotNull));
        Assert.assertTrue(result.getType().equals(Result.Type.success));
        Assert.assertTrue(result.getResultFilter().getType().equals(FilterType.likeStart));
        Assert.assertTrue(result.getResultFilter().getValue().equals("test"));

        result = reduce(
                new Filter("test", FilterType.likeStart),
                new Filter(FilterType.isNull));
        Assert.assertTrue(result.getType().equals(Result.Type.conflict));
        Assert.assertTrue(result.getResultFilter() == null);

        result = reduce(
                new Filter("test", FilterType.likeStart),
                new Filter("te", FilterType.likeStart));
        Assert.assertTrue(result.getType().equals(Result.Type.success));
        Assert.assertTrue(result.getResultFilter().getType().equals(FilterType.likeStart));
        Assert.assertTrue(result.getResultFilter().getValue().equals("test"));

        result = reduce(
                new Filter("rem", FilterType.likeStart),
                new Filter("te", FilterType.likeStart));
        Assert.assertTrue(result.getType().equals(Result.Type.conflict));
        Assert.assertTrue(result.getResultFilter() == null);

        result = reduce(
                new Filter("test", FilterType.likeStart),
                new Filter("es", FilterType.likeStart));
        Assert.assertTrue(result.getType().equals(Result.Type.conflict));
        Assert.assertTrue(result.getResultFilter() == null);
    }
}
