package net.n2oapp.criteria.api.constructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: marat
 * Date: 28.11.12
 * Time: 9:39
 */
public class CriteriaFieldCollector {
    private Map<String, Set<CriteriaField>> orGroups = new HashMap<String, Set<CriteriaField>>();

    private Set<CriteriaField> fields = new HashSet<CriteriaField>();

    public void put(String orGroupKey, CriteriaField field) {
        if (orGroupKey != null) {
            if (orGroups.get(orGroupKey) == null) {
                orGroups.put(orGroupKey, new HashSet<CriteriaField>());
            }
            orGroups.get(orGroupKey).add(field);
        } else {
            fields.add(field);
        }
    }

    public Map<String, Set<CriteriaField>> getOrGroups() {
        return orGroups;
    }

    public Set<CriteriaField> getFields() {
        return fields;
    }
}
