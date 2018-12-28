package net.n2oapp.criteria.dataset;

import org.junit.Test;
import net.n2oapp.criteria.dataset.Interval;

/**
 * User: operhod
 * Date: 01.11.13
 * Time: 12:29
 */
public class IntervalTest {

    private Interval interval1 = new Interval(1, 10);
    private Interval interval2 = new Interval(10, Interval.MAX);

    @Test
    public void checkIntervalWithTwoValues() {
        //входящие интервалы
        assert interval1.check(new Interval(1));
        assert interval1.check(new Interval(7));
        assert interval1.check(new Interval(6, 7));
        assert interval1.check(new Interval(1, 9));
        assert interval1.check(new Interval(1, 10));

        //интервалый выходящие за предел
        assert !interval1.check(new Interval(1, 56));
        assert !interval1.check(new Interval(17, 56));
        assert !interval1.check(new Interval(12));
        assert !interval1.check(new Interval(7, Interval.MAX));
    }


    @Test
    public void checkIntervalWithOneValue() {
        //входящие интервалы
        assert interval2.check(new Interval(11, 12));
        assert interval2.check(new Interval(11, 120));
        assert interval2.check(new Interval(17));

        //интервалый выходящие за предел
        assert !interval2.check(new Interval(9, 10));
        assert !interval2.check(new Interval(Interval.MIN, 99));
    }

    @Test
    public void equalsIntervalTest() {
        //равенство интервалов
        assert new Interval(1).equals(new Interval(1));
        assert new Interval(1, 10).equals(new Interval(1, 10));
        assert new Interval(Interval.MIN, 10).equals(new Interval(Interval.MIN, 10));
        assert new Interval(Interval.MIN, Interval.MAX).equals(new Interval(Interval.MIN, Interval.MAX));

        //неравенство
        assert !new Interval(1).equals(new Interval(2));
        assert !new Interval(1, 10).equals(new Interval(2, 10));
        assert !new Interval(Interval.MIN, 10).equals(new Interval(Interval.MIN, 11));
        assert !new Interval(Interval.MIN, Interval.MIN).equals(new Interval(Interval.MIN, Interval.MAX));

    }

}
