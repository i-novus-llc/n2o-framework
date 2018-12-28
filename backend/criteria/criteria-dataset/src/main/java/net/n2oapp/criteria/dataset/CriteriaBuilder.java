package net.n2oapp.criteria.dataset;

import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.api.CriteriaAccessor;

/**
 * User: operhod
 * Date: 29.04.14
 * Time: 15:51
 */
public class CriteriaBuilder {
    public static Criteria buildCriteria(DynamicCriteria criteria, Class<? extends Criteria> tClass) {
        if (tClass.equals(DynamicCriteria.class)) return criteria;
        Criteria res;
        try {
            res = tClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
        for (String key : criteria.keySet()) {
            CriteriaAccessor.getInstance().write(res, key, criteria.get(key));
        }
        res.setSortings(criteria.getSortings());
        res.setSize(criteria.getSize());
        res.setPage(criteria.getPage());
        res.setCount(criteria.getCount());
        return res;
    }
}
