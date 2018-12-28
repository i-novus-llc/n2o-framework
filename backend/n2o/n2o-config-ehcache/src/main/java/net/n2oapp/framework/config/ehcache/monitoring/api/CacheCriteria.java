package net.n2oapp.framework.config.ehcache.monitoring.api;

import net.n2oapp.criteria.api.Criteria;

/**
 * @author V. Alexeev.
 */
public class CacheCriteria extends Criteria {

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
