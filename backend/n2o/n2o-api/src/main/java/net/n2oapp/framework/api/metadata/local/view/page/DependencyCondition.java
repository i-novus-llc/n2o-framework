package net.n2oapp.framework.api.metadata.local.view.page;


import net.n2oapp.framework.api.script.ScriptProcessor;

import java.io.Serializable;
import java.util.*;

/**
 * User: operhod
 * Date: 04.06.14
 * Time: 16:05
 */
public class DependencyCondition implements Serializable {

    private Set<String> containerIds;
    private String condition;


    public DependencyCondition(String dependencyCondition, String pageId, Collection<String> pageContainers, ScriptProcessor scriptProcessor) {
        condition = scriptProcessor
                .addContextFor(dependencyCondition, pageId, pageContainers);
        Map<String, Set<String>> properties = scriptProcessor
                .extractPropertiesOf(condition, Arrays.asList(pageId));
        containerIds = new LinkedHashSet<>();
        if (properties.get(pageId) != null) {
            for (String container : properties.get(pageId)) {
                containerIds.add(pageId + "." + container);
            }
        }
    }

    public Set<String> getContainerIds() {
        return containerIds;
    }

    public String getCondition() {
        return condition;
    }

}
