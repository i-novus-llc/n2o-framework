package bean;

import net.n2oapp.framework.api.bean.BeansOrderException;
import net.n2oapp.framework.api.bean.BeansSorting;
import net.n2oapp.framework.api.bean.LocatedBean;
import net.n2oapp.framework.api.bean.LocatedBeanPack;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * User: operhod
 * Date: 25.11.13
 * Time: 12:03
 */
public class BeansSortingTest {


    @Test
    void testWithPacks() throws Exception {
        TestLocatedBean one = new TestLocatedBean(1);
        TestLocatedBean two = new TestLocatedBean(2);
        TestLocatedBean six = new TestLocatedBean(6);
        LocatedBeanPack<TestLocatedBean> pack1 = new LocatedBeanPack<>(TestLocatedBean.class, Arrays.asList(new TestLocatedBean(3), new TestLocatedBean(4), new TestLocatedBean(5)));
        LocatedBeanPack<TestLocatedBean> pack2 = new LocatedBeanPack<>(TestLocatedBean.class, Arrays.asList(new TestLocatedBean(7)));

        //задаем правила
        one.setBeforeAll(true);
        two.setPrevBeans(one);
        six.setAfterAll(true);
        pack1.setBefore(six);
        pack1.setAfter(one, two);
        pack2.setAfter(six);

        //готовим коллекцию
        List<TestLocatedBean> collection = Arrays.asList(one, two, six);
        Collections.shuffle(collection);

        collection = BeansSorting.sort(collection, Arrays.asList(pack1, pack2));
        System.out.println(collection.stream().map(b -> b.order).collect(Collectors.toList()));
        assert collection.stream().map(b -> b.order).collect(Collectors.toList()).equals(Arrays.asList(1, 2, 3, 4, 5, 6, 7));

    }


    @Test
    void sortTwoBeans() throws BeansOrderException {
        assertThrows(BeansOrderException.class, () -> {
            TestLocatedBean one;
            TestLocatedBean two;
            List<TestLocatedBean> res = null;

            one = new TestLocatedBean(1);
            two = new TestLocatedBean(2);
            //one должен идте после two
            one.setPrevBeans(two);

            try {
                res = BeansSorting.sort(Arrays.asList(one, two));
            } catch (BeansOrderException e) {
                assert false;
            }
            assert res.get(0).order == 2;
            assert res.get(1).order == 1;


            one = new TestLocatedBean(1);
            two = new TestLocatedBean(2);
            //one должен идте до two
            one.setNextBeans(two);
            //two должен идти после one
            two.setPrevBeans(one);

            try {
                res = BeansSorting.sort(Arrays.asList(one, two));
            } catch (BeansOrderException e) {
                assert false;
            }
            assert res.get(0).order == 1;
            assert res.get(1).order == 2;

            one = new TestLocatedBean(1);
            two = new TestLocatedBean(2);
            //исключительная ситуация
            one.setNextBeans(two);
            two.setNextBeans(one);
            BeansSorting.sort(Arrays.asList(one, two));
        });
    }

    @Test
    void sortThreeBeans() throws BeansOrderException {
        assertThrows(BeansOrderException.class, () -> {
            TestLocatedBean one;
            TestLocatedBean two;
            TestLocatedBean three;
            List<TestLocatedBean> res = null;

            one = new TestLocatedBean(1);
            two = new TestLocatedBean(2);
            three = new TestLocatedBean(3);
            one.setPrevBeans(two);
            two.setPrevBeans(three);
            try {
                res = BeansSorting.sort(Arrays.asList(one, two, three));
            } catch (BeansOrderException e) {
                assert false;
            }
            assert res.get(0).order == 3;
            assert res.get(1).order == 2;
            assert res.get(2).order == 1;


            one = new TestLocatedBean(1);
            two = new TestLocatedBean(2);
            three = new TestLocatedBean(3);
            one.setNextBeans(two, three);
            three.setPrevBeans(one, two);
            try {
                res = BeansSorting.sort(Arrays.asList(one, two, three));
            } catch (BeansOrderException e) {
                assert false;
            }
            assert res.get(0).order == 1;
            assert res.get(1).order == 2;
            assert res.get(2).order == 3;

            one = new TestLocatedBean(1);
            two = new TestLocatedBean(2);
            three = new TestLocatedBean(3);
            two.setNextBeans(one);
            one.setNextBeans(three);
            three.setPrevBeans(one, two);
            try {
                res = BeansSorting.sort(Arrays.asList(one, two, three));
            } catch (BeansOrderException e) {
                assert false;
            }
            assert res.get(0).order == 2;
            assert res.get(1).order == 1;
            assert res.get(2).order == 3;

            one = new TestLocatedBean(1);
            two = new TestLocatedBean(2);
            three = new TestLocatedBean(3);
            //исключительная ситуация
            one.setPrevBeans(three);
            three.setPrevBeans(two);
            two.setPrevBeans(one);
            BeansSorting.sort(Arrays.asList(one, two, three));
        });
    }

    @Test
    void beforeAndAfterAllTest() {
        for (int i = 0; i < 10; i++) {
            afterAllTest();
            beforeAllTest();
        }
    }

    void afterAllTest() {
        TestLocatedBean one;
        TestLocatedBean two;
        TestLocatedBean three;
        List<TestLocatedBean> res = null;

        one = new TestLocatedBean(1);
        two = new TestLocatedBean(2);
        three = new TestLocatedBean(3);
        one.setAfterAll(true);
        two.setPrevBeans(three);
        try {
            res = BeansSorting.sort(Arrays.asList(one, two, three));
        } catch (BeansOrderException e) {
            assert false;
        }
        assert res.get(0).order == 3;
        assert res.get(1).order == 2;
        assert res.get(2).order == 1;

        one = new TestLocatedBean(1);
        two = new TestLocatedBean(2);
        three = new TestLocatedBean(3);
        one.setAfterAll(true);
        one.setPrevBeans(three);
        two.setPrevBeans(three);
        try {
            res = BeansSorting.sort(Arrays.asList(one, two, three));
        } catch (BeansOrderException e) {
            assert false;
        }
        assert res.get(0).order == 3;
        assert res.get(1).order == 2;
        assert res.get(2).order == 1;
    }

    void beforeAllTest() {
        TestLocatedBean one;
        TestLocatedBean two;
        TestLocatedBean three;
        List<TestLocatedBean> res = null;

        one = new TestLocatedBean(1);
        two = new TestLocatedBean(2);
        three = new TestLocatedBean(3);
        one.setBeforeAll(true);
        two.setPrevBeans(three);
        try {
            res = BeansSorting.sort(Arrays.asList(one, two, three));
        } catch (BeansOrderException e) {
            assert false;
        }
        assert res.get(0).order == 1;
        assert res.get(1).order == 3;
        assert res.get(2).order == 2;

        one = new TestLocatedBean(1);
        two = new TestLocatedBean(2);
        three = new TestLocatedBean(3);
        one.setBeforeAll(true);
        one.setNextBeans(three);
        two.setPrevBeans(three);
        try {
            res = BeansSorting.sort(Arrays.asList(one, two, three));
        } catch (BeansOrderException e) {
            assert false;
        }
        assert res.get(0).order == 1;
        assert res.get(1).order == 3;
        assert res.get(2).order == 2;
    }


    private static class TestLocatedBean implements LocatedBean {

        private TestLocatedBean[] prevBeans;
        private TestLocatedBean[] nextBeans;
        private boolean beforeAll;
        private boolean afterAll;
        private int order;

        private TestLocatedBean(int order) {
            this.order = order;
        }


        @Override
        public boolean isAfterAll() {
            return afterAll;
        }

        @Override
        public boolean isBeforeAll() {
            return beforeAll;
        }


        @Override
        public LocatedBean[] getNextBeans() {
            return nextBeans;
        }

        @Override
        public LocatedBean[] getPrevBeans() {
            return prevBeans;
        }


        private void setPrevBeans(TestLocatedBean... prevBeans) {
            this.prevBeans = prevBeans;
        }

        private void setNextBeans(TestLocatedBean... nextBeans) {
            this.nextBeans = nextBeans;
        }

        private void setAfterAll(boolean afterAll) {
            this.afterAll = afterAll;
        }

        private void setBeforeAll(boolean beforeAll) {
            this.beforeAll = beforeAll;
        }

    }

}
