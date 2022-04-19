package net.n2oapp.framework.api.metadata.global.dao;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Source модель запроса за данными
 */
@Getter
@Setter
public class N2oQuery extends N2oMetadata implements NameAware, ExtensionAttributesAware {
    protected Field[] fields;
    private String name;
    private String objectId;
    private String route;
    private Selection[] lists;
    private Selection[] uniques;
    private Selection[] counts;
    private Map<N2oNamespace, Map<String, String>> extAttributes;

    @Override
    public final String getPostfix() {
        return "query";
    }

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oQuery.class;
    }

    @Getter
    @Setter
    public static class Filter implements Source, Compiled {
        private String text;
        private String defaultValue;
        private Object compiledDefaultValue;
        private FilterType type;
        private String filterField; //todo rename to filterId
        private String domain;
        private String normalize;
        private String mapping;
        private Boolean required;

        /**
         * Параметр в адресе маршрутов выборки
         */
        private String param;
        private boolean generated = false;

        public Filter(String filterField, FilterType type) {
            this.filterField = filterField;
            this.type = type;
        }

        public Filter(String filterField, FilterType type, String filterBody) {
            this.text = filterBody;
            this.filterField = filterField;
            this.type = type;
        }

        public Filter(String filterField, FilterType type, String filterBody, String domain) {
            this.filterField = filterField;
            this.type = type;
            this.text = filterBody;
            this.domain = domain;
        }

        public Filter(String filterField, FilterType type, String filterBody, String domain, String normalize) {
            this.type = type;
            this.filterField = filterField;
            this.domain = domain;
            this.normalize = normalize;
            this.text = filterBody;
        }

        public Filter() {
        }

    }

    @Getter
    @Setter
    public static class Field implements Source, Compiled, NameAware, IdAware {
        public static final String PK = "id";
        public static List<String> INTERVAL_ATTRIBUTE = Arrays.asList("begin", "end");
        public static final String MULTI_ATTRIBUTE = "*";

        public static String getReference(String fieldId) {
            if (fieldId == null)
                return null;
            int idx = fieldId.lastIndexOf(".");
            if (idx < 0) return fieldId;
            if (isMulti(fieldId))
                idx = fieldId.lastIndexOf(MULTI_ATTRIBUTE);
            return fieldId.substring(0, idx);
        }

        private String id;
        private String name;
        private String domain;
        private String expression;
        private String sortingBody;
        private String sortingMapping;
        private String selectBody; //= select body
        private String selectDefaultValue; // = default value in select
        private String selectMapping; // = selectMapping in select
        private String normalize;
        private Filter[] filterList;

        private String joinBody;
        private Boolean noSorting;
        private Boolean noDisplay;

        private Boolean noJoin;

        public Field(String id) {
            setId(id);
        }

        public Field() {
        }

        public boolean isFK() {
            ArrayList<String> suffix = new ArrayList<>(INTERVAL_ATTRIBUTE);
            suffix.add(PK);
            return suffix.stream().anyMatch(s -> getId().endsWith("." + s));
        }

        public boolean isPK() {
            return getId().equalsIgnoreCase(PK);
        }

        public boolean isSelf() {
            return ((!isHasPoint()) || (isFK() && isPlaneReference()));
        }

        private boolean isPlaneReference() {
            return (!getOwnReference().contains("."));
        }

        public boolean isHasPoint() {
            return getId().contains(".");
        }

        public String getOwnReference() {
            return getReference(getId());
        }

        public Type getType() {
            if (!isHasPoint()) {
                return Type.simple;
            } else if (isPlaneReference() && INTERVAL_ATTRIBUTE.stream().anyMatch(a -> getId().endsWith("." + a))) {
                return Type.interval;
            } else if (isFK() && !isMulti()) {
                return Type.list;
            } else if (isMulti()) {
                return Type.multi;
            } else
                return null;
        }

        public boolean isMulti() {
            return isMulti(getId());
        }

        public static boolean isMulti(String fieldId) {
            return fieldId != null && fieldId.contains(MULTI_ATTRIBUTE + ".");
        }

        public Boolean getHasSorting() {
            return noSorting == null ? null : !noSorting;
        }

        public void setHasSorting(Boolean hasSorting) {
            this.noSorting = !hasSorting;
        }

        public Boolean getHasSelect() {
            return noDisplay == null ? null : !noDisplay;
        }

        public void setHasSelect(Boolean hasDisplay) {
            this.noDisplay = !hasDisplay;
        }

        public Boolean getHasJoin() {
            return noJoin == null ? null : !noJoin;
        }

        public void setHasJoin(Boolean hasJoin) {
            this.noJoin = !hasJoin;
        }


        public boolean isSearchUnavailable() {
            return getFilterList() == null;
        }

        @Override
        public String toString() {
            return id;
        }

        public enum Type {
            simple,
            list,
            interval,
            multi
        }
    }

    @Getter
    @Setter
    public static class Selection implements Source, Compiled {
        private String filters;
        private String resultMapping;
        private String countMapping;
        private N2oInvocation invocation;
        private Type type;

        public Selection(Type type) {
            this.type = type;
        }

        public Selection(Type type, N2oInvocation invocation) {
            this(type);
            this.invocation = invocation;
        }


        public enum Type {
            list, unique, count
        }
    }

}
