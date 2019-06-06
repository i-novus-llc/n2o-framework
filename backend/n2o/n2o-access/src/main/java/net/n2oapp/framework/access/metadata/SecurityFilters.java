package net.n2oapp.framework.access.metadata;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFilter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Фильтрация объектов в правах доступа
 */
@Setter
@Getter
public class SecurityFilters implements Serializable {
    /**
     * Ключ объекта Security в properties
     */
    public static String SECURITY_FILTERS_PROP_NAME = "securityFilters";

    /**
     * Фильтры объектов, соответствующие ролям
     * В map ключом является идентификатор роли
     */
    private Map<String, List<N2oObjectFilter>> roleFilters;

    /**
     * Фильтры объектов, соответствующие привелегиям
     * В map ключом является идентификатор привелегии
     */
    private Map<String, List<N2oObjectFilter>> permissionFilters;

    /**
     * Фильтры объектов, соответствующие доступу по имени пользователя
     * В map ключом является имя пользователя,
     */
    private Map<String, List<N2oObjectFilter>> userFilters;

    /**
     * Фильтры объектов, применяемые всем авторизованным пользователям
     */
    private List<N2oObjectFilter> authenticatedFilters;

    /**
     * Фильтры объектов, применяемые всем не авторизованным пользователям
     */
    private List<N2oObjectFilter> anonymousFilters;

    /**
     * Фильтры объектов, применяемые всем пользователям
     * В map ключом является идентификатор объекта
     */
    private List<N2oObjectFilter> permitAllFilters;

    /**
     * Фильтры объектов, исключенные из ролей
     * В map ключом является идентификатор роли
     */
    private Map<String, Set<String>> removeRoleFilters;

    /**
     * Фильтры объектов, исключенные из привелегий
     * В map ключом является идентификатор привелегии
     */
    private Map<String, Set<String>> removePermissionFilters;

    /**
     * Фильтры объектов, исключенные у пользователей
     * В map ключом является имя пользователя
     */
    private Map<String, Set<String>> removeUserFilters;

    /**
     * Фильтры объектов, исключенные у всех авторизованных пользователей
     */
    private Set<String> removeAuthenticatedFilters;

    /**
     * Фильтры объектов, исключенные у всех не авторизованных пользователей
     */
    private Set<String> removeAnonymousFilters;

    /**
     * Фильтры объектов, исключенные у всех пользователей
     */
    private Set<String> removePermitAllFilters;

    public void addRemovePermitAllFilters(String filter) {
        if (removePermitAllFilters == null)
            removePermitAllFilters = new HashSet<>();
        removePermitAllFilters.add(filter);
    }

    public void addRemoveAnonymousFilters(String filter) {
        if (removeAnonymousFilters == null)
            removeAnonymousFilters = new HashSet<>();
        removeAnonymousFilters.add(filter);
    }

    public void addRemoveAuthenticatedFilters(String filter) {
        if (removeAuthenticatedFilters == null)
            removeAuthenticatedFilters = new HashSet<>();
        removeAuthenticatedFilters.add(filter);
    }

}
