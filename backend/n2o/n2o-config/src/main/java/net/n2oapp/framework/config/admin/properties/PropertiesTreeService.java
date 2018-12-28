package net.n2oapp.framework.config.admin.properties;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.api.FilteredCollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author operehod
 * @since 04.08.2015
 */
@Component
public class PropertiesTreeService implements CollectionPageService<Criteria, DataSet> {


    private Properties n2oProperties;

    public PropertiesTreeService(Properties n2oProperties) {
        this.n2oProperties = n2oProperties;
    }


    @Override
    public CollectionPage<DataSet> getCollectionPage(Criteria criteria) {
        Set<String> names = n2oProperties.stringPropertyNames();
        Map<String, DataSet> firstLvl = new LinkedHashMap<>();
        Map<String, DataSet> secondLvl = new LinkedHashMap<>();
        names.forEach(name -> {
            String s[] = name.split(Pattern.quote("."));
            if (s.length > 2) {
                String parent = s[0];
                String child = parent + "." + s[1];
                firstLvl.put(parent, new DataSet("id", parent).add("name", parent).add("hasChildren", true).add("canResolved", true).add("parent", null));
                secondLvl.put(child, new DataSet("id", child).add("name", child).add("hasChildren", false).add("canResolved", true).add("parent", parent));
            }

        });
        List<DataSet> res = new ArrayList<>(firstLvl.values());
        res.addAll(secondLvl.values());
        return new FilteredCollectionPage<>(res, criteria);
    }
}
