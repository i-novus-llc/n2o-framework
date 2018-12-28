package net.n2oapp.framework.config.admin.properties;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.criteria.api.FilteredCollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.properties.io.PropertiesInfoCollector;
import net.n2oapp.properties.web.WebApplicationProperties;
import net.n2oapp.properties.web.WebApplicationProperties.PropertyValue;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

import static java.util.stream.Collectors.toList;

/**
 * @author operehod
 * @since 04.08.2015
 */
@Component
public class PropertiesService implements CollectionPageService<PropertiesCriteria, DataSet> {


    private WebApplicationProperties n2oProperties;
    private PropertiesInfoCollector collector;

    public PropertiesService(Properties n2oProperties, PropertiesInfoCollector collector) {
        this.n2oProperties = (WebApplicationProperties) n2oProperties;
        this.collector = collector;
    }

    @Override
    public CollectionPage<DataSet> getCollectionPage(PropertiesCriteria criteria) {
        if (criteria.getId() != null) {
            String propertyName = criteria.getId();
            PropertyValue value = n2oProperties.getFullPropertyValue(propertyName);
            PropertiesInfoCollector.PropertyInfo info = collector.getInfo(collector.getGroup(criteria.getId()), criteria.getId());
            DataSet res = new DataSet("id", propertyName)
                    .add("value", checkForEmpty(secure(propertyName, value.getValue())))
                    .add("defaultValue", checkForEmpty(secure(propertyName, value.getDefaultValue())))
                    .add("buildValue", checkForEmpty(secure(propertyName, value.getBuildValue())))
                    .add("envValue", secure(propertyName, value.getEnvValue()))
                    .add("hasEnvValue", value.getEnvValue() != null)
                    .add("servletValue", secure(propertyName, value.getServletValue()))
                            .add("hasServletValue", value.getServletValue() != null)
                            .add("group", checkGroup(info.group))
                            .add("name", info.name)
                            .add("description", info.description);
            return new FilteredCollectionPage<>(Collections.singletonList(res), criteria);
        }


        return new FilteredCollectionPage<>(
                n2oProperties.stringPropertyNames()
                        .stream()
                        .filter(name -> criteria.getPropertiesName() == null || name.toLowerCase().startsWith(criteria.getPropertiesName().toLowerCase()))
                        .sorted(Comparator.naturalOrder())
                        .map(p -> new DataSet("id", p).add("value", checkForEmpty(secure(p, n2oProperties.getProperty(p))))
                                .add("group", collector.getGroup(p)))
                        .collect(toList()),
                criteria);
    }

    private String checkForEmpty(String value) {
        if (value == null)
            return "<value not exists>";
        else if (value.isEmpty())
            return "<empty>";
        return value;
    }

    private String secure(String propertyName, String value) {
        if (value == null || value.isEmpty())
            return value;
        if (propertyName.contains("password") || propertyName.contains("passphrase")) {
            return "*****";
        }
        return value;
    }

    private String checkGroup(String value) {
        if (value == null || value.equalsIgnoreCase("common")) {
            return null;
        } return value;
    }

    public void update(String key, String envValue, String servletValue, Boolean hasEnv, Boolean hasServ) {
        final PropertyValue val = n2oProperties.getFullPropertyValue(key);
        if (val.getEnvValue() != null && (hasEnv == null || !hasEnv)) {
            n2oProperties.getEnvProperties().removeProperty(key);
        } else if ((val.getEnvValue() != null || (hasEnv != null && hasEnv))
                && (val.getEnvValue() == null || !val.getEnvValue().equals(envValue))){
            n2oProperties.getEnvProperties().updateProperty(key, envValue);
        }

        if (val.getServletValue() != null && (hasServ == null || !hasServ)) {
            n2oProperties.getServletProperties().removeProperty(key);
        } else if ((val.getServletValue() != null || (hasServ != null && hasServ)
                && (val.getServletValue() == null || !val.getServletValue().equals(servletValue))) ){
            n2oProperties.getServletProperties().updateProperty(key, servletValue);
        }
    }


}
