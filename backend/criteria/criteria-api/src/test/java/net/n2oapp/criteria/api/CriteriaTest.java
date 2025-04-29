package net.n2oapp.criteria.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CriteriaTest {

    @Test
    void testSorting() {
        Criteria criteria = new Criteria();
        //check initial setting
        criteria.addSorting(new Sorting("test1", SortingDirectionEnum.DESC));
        assertNotNull(criteria.getSorting());
        assertEquals("test1", criteria.getSorting().getField());
        assertEquals(SortingDirectionEnum.DESC, criteria.getSorting().getDirection());
        assertEquals(1, criteria.getSortings().size());

        //check re-setting
        criteria.addSorting(new Sorting("test2", SortingDirectionEnum.ASC));
        assertNotNull(criteria.getSorting());
        assertEquals("test2", criteria.getSorting().getField());
        assertEquals(SortingDirectionEnum.ASC, criteria.getSorting().getDirection());
        assertEquals(2, criteria.getSortings().size());
        assertEquals("test1", criteria.getSortings().get(1).getField());
        assertEquals(SortingDirectionEnum.DESC, criteria.getSortings().get(1).getDirection());
    }
}
