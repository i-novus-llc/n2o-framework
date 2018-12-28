package net.n2oapp.framework.config.admin.properties;

import net.n2oapp.criteria.api.Criteria;

/**
 * @author operehod
 * @since 04.08.2015
 */
public class PropertiesCriteria extends Criteria {

    private String propertiesName;
    private String id;
    private String group;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPropertiesName() {
        return propertiesName;
    }

    public void setPropertiesName(String propertiesName) {
        this.propertiesName = propertiesName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
